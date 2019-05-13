package wolox.training.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.RequiredFieldNotExists;
import wolox.training.model.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.service.OpenLibraryService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
        return bookRepository.save(book);
    }

    @GetMapping("/search")
    public Book search(@RequestParam("isbn") String isbn) {
        try {
            return bookRepository.findByIsbn(isbn).
                orElse(
                    openLibraryService.bookInfo(isbn).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Book with ISBN " + isbn + " not found"))
                );
        } catch (RequiredFieldNotExists ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        }
    }

}
