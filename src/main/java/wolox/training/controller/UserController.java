package wolox.training.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.exceptions.UserIdMismatchException;
import wolox.training.model.Book;
import wolox.training.model.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void addBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + bookId + " not found"));
        user.addBook(book);
        userRepository.save(user);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id " + id + " not found"));
        userRepository.deleteById(id);
    }

    @DeleteMapping("/{userId}/books/{bookId}")
    public void removeBook(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id " + userId + " not found"));
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + bookId + " not found"));
        user.removeBook(book);
        userRepository.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user == null || (user.getId() != id)) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with id " + id + " not found"));
        return userRepository.save(user);
    }

    @GetMapping("/username")
    @ResponseBody
    public String currentUserName(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/search")
    public List<User> findByBirthdateAndName(@RequestParam("from") String from,
        @RequestParam("to") String to, @RequestParam("name") String name) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate.parse(from, formatter),
                LocalDate.parse(to, formatter), name).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Not found"));
    }

}
