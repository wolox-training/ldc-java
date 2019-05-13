package wolox.training.service;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.model.Book;
import wolox.training.model.BookDTO;

@Service
public class OpenLibraryService {

    private static final String API_URL = "https://openlibrary.org/api/books?bibkeys=ISBN:";
    private static final String FORMAT_PARAMETER = "&format=json&jscmd=data";

    /**
     * Build an Optional<BookDTO> from the Optional<BookDTO>
     *
     * @param optionalBookDTO is the BookDTO built from the JSON response (If exists)
     * @return Optional<Book> with the book if the book exists, or Optional.empty otherwise
     */
    private Optional<Book> convertBookDTOtoBook(Optional<BookDTO> optionalBookDTO) {
        try {
            Book book = new Book();
            BookDTO bookDTO = optionalBookDTO.orElseThrow(BookNotFoundException::new);
            book.setIsbn(bookDTO.getIsbn());
            book.setAuthor(bookDTO.getAuthors());
            book.setYear(bookDTO.getPublishDate());
            book.setPublisher(bookDTO.getPublisher());
            book.setSubtitle(bookDTO.getSubtitle());
            book.setTitle(bookDTO.getTitle());
            book.setPages(bookDTO.getPages());
            return Optional.of(book);
        } catch (BookNotFoundException ex) {
            return Optional.empty();
        }
    }

    public Optional<Book> bookInfo(String isbn) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
            .getForEntity(API_URL + isbn + FORMAT_PARAMETER, String.class);
        Optional<BookDTO> optionalBookDTO = BookDTO.buildBookDTO(response.getBody());
        return (this.convertBookDTOtoBook(optionalBookDTO));
    }

}
