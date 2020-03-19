package wolox.training.models;

import com.google.common.base.Preconditions;
import wolox.training.utils.MessageConstants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOOK_SEQ")
    @SequenceGenerator(name = "BOOK_SEQ", sequenceName = "book_id_seq", allocationSize = 1)
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    private List<User> users;

    public Book() {

    }

    public long getId() {
        return id;
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
        Preconditions.checkArgument(author != null && !author.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("author"));
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        Preconditions.checkArgument(image != null && !image.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("image"));
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        Preconditions.checkArgument(title != null && !title.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("title"));
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        Preconditions.checkArgument(subtitle != null && !subtitle.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("subtitle"));
        this.subtitle = subtitle;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        Preconditions.checkArgument(publisher != null && !publisher.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("publisher"));
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        Preconditions.checkArgument(year != null && !year.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("year"));
        this.year = year;
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
        Preconditions.checkArgument(isbn != null && !isbn.isEmpty(),
                MessageConstants.getIllegalArgumentMessage("isbn"));
        this.isbn = isbn;
    }

    public List<User> getUsers() {
        return users;
    }
}
