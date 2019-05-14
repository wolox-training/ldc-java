package wolox.training.model;

import com.google.common.base.Preconditions;
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
    private String password;

    @NotNull
    private String name;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH})
    private List<Book> books;

    public User() {
        this.books = new ArrayList<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = Preconditions.checkNotNull(id,
            "Illegal Argument, id cannot be NULL.");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Preconditions.checkNotNull(username,
            "Illegal Argument, username cannot be NULL.");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Preconditions.checkNotNull(name,
            "Illegal Argument, name cannot be NULL.");
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = Preconditions.checkNotNull(birthdate,
            "Illegal Argument, birthdate cannot be NULL.");
    }

    public List<Book> getBooks() {
        return (List<Book>) Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = Preconditions.checkNotNull(books,
            "Illegal Argument, books cannot be NULL.");
    }

    /**
     * Add a book to the books of the user. If the book already exists, then a
     * BookAlreadyOwnedException is thrown.
     *
     * @param {@link Book}
     * @throws {@link BookAlreadyOwnedException}
     */
    public void addBook(Book book) {
        if (!books.contains(book)) {
            books.add(book);
        } else {
            throw new BookAlreadyOwnedException();
        }
    }

    /**
     * Remove a book from the books of the user. If the book doesn't exist, nothing happends
     *
     * @param {@link Book}
     */
    public void removeBook(Book book) {
        books.remove(book);
    }
}
