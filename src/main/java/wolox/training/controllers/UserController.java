package wolox.training.controllers;

import java.security.Principal;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
import wolox.training.exceptions.BookNotFoundException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.exceptions.UserNotFoundException;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    @SuppressWarnings("unused")
    private static UserRepository userRepository;

    @Autowired
    @SuppressWarnings("unused")
    private static BookRepository bookRepository;

    public static void setBookRepository(BookRepository bookRepository) {
        UserController.bookRepository = bookRepository;
    }

    public static void setUserRepository(UserRepository userRepository) {
        UserController.userRepository = userRepository;
    }

    public UserController(
        @Autowired UserRepository userRepository,
        @Autowired BookRepository bookRepository) {
        UserController.setUserRepository(userRepository);
        UserController.setBookRepository(bookRepository);
    }

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with id " + bookId + " not found");
        }
        user.addBook(book);
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }
        userRepository.delete(id);
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    public void removeBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }
        Book book = bookRepository.findOne(bookId);
        if (book == null) {
            throw new BookNotFoundException("Book with id " + bookId + " not found");
        }
        user.removeBook(book);
        userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user == null || (user.getId() != id)) {
            throw new UserIdMismatchException();
        }

        User userFound = userRepository.findOne(id);
        if (userFound == null) {
            throw new UserNotFoundException("User with id " + id + " not found");
        }

        return userRepository.save(user);
    }

    @GetMapping("/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/search")
    public Page<User> findByBirthdateAndName(
        @RequestParam(required = false, defaultValue = "0000-01-01") String from,
        @RequestParam(required = false, defaultValue = "9999-12-31") String to,
        @RequestParam(required = false, defaultValue = "") String name,
        Pageable pageable) {
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        Page<User> usersFound = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                LocalDate.parse(from, formatter),
                LocalDate.parse(to, formatter), name, pageable);
        if (usersFound == null) {
            throw new UserNotFoundException("Not Found");
        }
        return usersFound;
    }

}
