package wolox.training.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.google.common.base.Preconditions;
import wolox.training.exceptions.BookAlreadyOwnedException;
import wolox.training.utils.MessageConstants;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
    @SequenceGenerator(name = "USER_SEQ", sequenceName = "users_id_seq", allocationSize = 1)
    @SuppressWarnings("unused")
    private long id;

    @NotNull
    private String username;

    @NotNull
    @JsonProperty(access = Access.WRITE_ONLY)
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        Preconditions.checkArgument(username != null && !username.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("username"));
        this.username = username;
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
        Preconditions.checkArgument(name != null && !name.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("name"));
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = Preconditions.checkNotNull(birthdate,
            "Illegal Argument, birthdate cannot be NULL.");
    }

    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    public void setBooks(List<Book> books) {
        this.books = Preconditions.checkNotNull(books,
            "Illegal Argument, books cannot be NULL.");
    }

    /**
     * Add a book to the books of the user. If the book already exists, then a
     * BookAlreadyOwnedException is thrown.
     *
     * @param book {@link Book}
     * @throws BookAlreadyOwnedException if the book is already owned by the user
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
     * @param book {@link Book}
     */
    public void removeBook(Book book) {
        books.remove(book);
    }
}
