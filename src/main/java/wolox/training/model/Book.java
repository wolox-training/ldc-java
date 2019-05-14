package wolox.training.model;

import com.google.common.base.Preconditions;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String genre;

    @NotNull
    private String author;

    @NotNull
    private String image;

    @NotNull
    private String title;

    @NotNull
    private String subtitle;

    @NotNull
    private String publisher;

    @NotNull
    private String year;

    @NotNull
    private Integer pages;

    @NotNull
    private String isbn;

    @ManyToMany(mappedBy = "books")
    private List<User> users;

    public Book() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = Preconditions.checkNotNull(id,
            "Illegal Argument, id cannot be NULL.");
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = Preconditions.checkNotNull(author,
            "Illegal Argument, author cannot be NULL.");
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = Preconditions.checkNotNull(image,
            "Illegal Argument, image cannot be NULL.");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Preconditions.checkNotNull(title,
            "Illegal Argument, title cannot be NULL.");
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = Preconditions.checkNotNull(subtitle,
            "Illegal Argument, subtitle cannot be NULL.");
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = Preconditions.checkNotNull(publisher,
            "Illegal Argument, publisher cannot be NULL.");
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = Preconditions.checkNotNull(year,
            "Illegal Argument, year cannot be NULL.");
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        Preconditions.checkArgument(pages != null && pages > 0,
            "Illegal Argument, pages has to be greater than 0.");
        this.pages = pages;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = Preconditions.checkNotNull(isbn,
            "Illegal Argument, isbn cannot be NULL.");
    }
}
