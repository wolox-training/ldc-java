package wolox.training.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.model.Book;
import wolox.training.model.BookDTO;
import wolox.training.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Build an Optional<BookDTO> from the Optional<BookDTO>
     *
     * @param optionalBookDTO is the BookDTO built from the JSON response (If exists)
     * @return Optional<Book> with the book if the book exists, or Optional.empty otherwise
     */
    private Optional<Book> convertBookDTOToBook(Optional<BookDTO> optionalBookDTO) {
        try {
            Book book = new Book();
            BookDTO bookDTO = optionalBookDTO.orElseThrow(BookNotFoundException::new);
            book.setIsbn(bookDTO.getIsbn());
            book.setAuthor(bookDTO.getAuthors());
            book.setImage(bookDTO.getImage());
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

    public Optional<Book> findByIsbn(String isbn) {
        Optional<BookDTO> optionalBookDTO = openLibraryService.bookInfo(isbn);
        Optional<Book> optionalBook = this.convertBookDTOToBook(optionalBookDTO);
        bookRepository.save(optionalBook.get());
        return optionalBook;
    }

}
