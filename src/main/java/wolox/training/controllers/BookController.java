package wolox.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.RequiredFieldNotExists;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    @SuppressWarnings("unused")
    private static BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unused")
    private static BookService bookService;

    public static void setBookRepository(BookRepository bookRepository) {
        BookController.bookRepository = bookRepository;
    }

    public static void setBookService(BookService bookService) {
        BookController.bookService = bookService;
    }

    BookController(@Autowired BookRepository bookRepository, @Autowired BookService bookService) {
        BookController.setBookRepository(bookRepository);
        BookController.setBookService(bookService);
    }

    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public Book findOne(@PathVariable Long id) {
        Book book = bookRepository.findOne(id);
        if (book == null) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        return book;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Book book = bookRepository.findOne(id);
        if (book == null) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        bookRepository.delete(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        Book foundBook = bookRepository.findOne(id);
        if (foundBook == null) {
            throw new BookNotFoundException("Book with id " + id + " not found");
        }
        return bookRepository.save(book);
    }

    @GetMapping("/search-by-isbn")
    public ResponseEntity<Book> search(@RequestParam("isbn") String isbn) {
        try {
            Book optionalBook = bookRepository.findByIsbn(isbn);
            if (optionalBook != null) {
                return (ResponseEntity.status(HttpStatus.OK).body(optionalBook));
            } else {
                Book foundBook = bookService.findByIsbn(isbn);
                if (foundBook == null) {
                    throw new BookNotFoundException("Book with ISBN " + isbn + " not found");
                }
                return ResponseEntity.status(HttpStatus.CREATED).body(foundBook);
            }
        } catch (RequiredFieldNotExists ex) {
            throw ex;
        }
    }

    @GetMapping
    @ResponseBody
    public Page<Book> getBooks(@RequestParam(required = false, defaultValue = "") String id,
        @RequestParam(required = false, defaultValue = "") String genre,
        @RequestParam(required = false, defaultValue = "") String author,
        @RequestParam(required = false, defaultValue = "") String image,
        @RequestParam(required = false, defaultValue = "") String title,
        @RequestParam(required = false, defaultValue = "") String subtitle,
        @RequestParam(required = false, defaultValue = "") String publisher,
        @RequestParam(required = false, defaultValue = "") String fromYear,
        @RequestParam(required = false, defaultValue = "") String toYear,
        @RequestParam(required = false, defaultValue = "") String pages,
        @RequestParam(required = false, defaultValue = "") String isbn,
        Pageable pageable) {

        Page<Book> foundBooks = bookRepository
            .findAllByEveryField(id, genre, author, image, title, subtitle, publisher,
                fromYear, toYear, pages, isbn, pageable);
        if (foundBooks == null || foundBooks.getSize() == 0) {
            throw new BookNotFoundException("Not found");
        }
        return foundBooks;
    }


}
