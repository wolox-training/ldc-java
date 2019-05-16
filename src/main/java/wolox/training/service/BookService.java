package wolox.training.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import wolox.training.model.Book;
import wolox.training.model.BookDTO;
import wolox.training.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @Autowired
    private ModelMapper modelMapper;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Build a Book from the BookDTO
     *
     * @param bookDto is the BookDTO built from the JSON response
     * @return {@link Book}
     */
    private Book convertToEntity(BookDTO bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }

    public Optional<Book> findByIsbn(String isbn) {
        Optional<BookDTO> optionalBookDTO = openLibraryService.bookInfo(isbn);
        Optional<Book> optionalBook = optionalBookDTO.map((bookDTO) -> {
            Book book = this.convertToEntity(bookDTO);
            bookRepository.save(book);
            return book;
        });
        return optionalBook;
    }

}
