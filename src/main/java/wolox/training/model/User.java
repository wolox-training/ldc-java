package wolox.training.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import wolox.training.exceptions.BookAlreadyOwnedException;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<Book> books;

    public User() {
        this.books = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableCollection(books);
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Add a book to the books of the user. If the book already exists, then a
     * BookAlreadyOwnedException is thrown.
     *
     * @param {@link Book}
     */
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        } else {
            throw new BookAlreadyOwnedException();
        }
    }

    /**
     * Remove a book from the books of the user. If the book doesn't exist, nothing happend
     *
     * @param {@link Book}
     */
    public void removeBook(Book book) {
        books.remove(book);
    }
}
