package wolox.training.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.DTO.BookDTO;
import wolox.training.repositories.BookRepository;

@Service
public class BookService {

    @Autowired
    @SuppressWarnings("unused")
    private BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unused")
    private OpenLibraryService openLibraryService;

    @Autowired
    @SuppressWarnings("unused")
    private ModelMapper modelMapper;

    @Bean
    @SuppressWarnings("unused")
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
        Book book = modelMapper.map(bookDto, Book.class);
        book.setPublisher(bookDto.getPublishers().iterator().next().getName());
        book.setAuthor(bookDto.getAuthors().iterator().next().getName());
        book.setImage(bookDto.getImage().getUrl());
        return book;
    }

    public Book findByIsbn(String isbn) {
        BookDTO optionalBookDTO = openLibraryService.bookInfo(isbn);
        if (optionalBookDTO != null) {
            Book book = this.convertToEntity(optionalBookDTO);
            bookRepository.save(book);
            return book;
        }
        throw new BookNotFoundException("Book ISBN not found in external service");
    }

}
