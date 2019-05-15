package wolox.training.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import wolox.training.exceptions.RequiredFieldNotExists;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BookDTO {

    private final static String TITLE_FIELD = "title";
    private final static String SUBTITLE_FIELD = "subtitle";
    private final static String COVER_FIELD = "cover";
    private final static String COVER_SIZE_FIELD = "small";
    private final static String PUBLISHER_FIELD = "publishers";
    private final static String NAME_FIELD = "name";
    private final static String PUBLISH_DATE_FIELD = "publish_date";
    private final static String PAGES_FIELD = "number_of_pages";
    private final static String AUTHORS_FIELD = "authors";

    private String isbn;
    private String title;
    private String subtitle;
    private String image;
    private String publisher;
    private String year;
    private Integer pages;
    private String author;

    private BookDTO() {

    }

    /**
     * Build a BookDTO from the JSON response. Checking that every required field exists.
     *
     * @param bookJson String with the JSON response (As String)
     * @return Optional<BookDTO> with the bookDTO if booKJson has every required field and the book
     * exists, or Optional.empty otherwise
     */
    public static Optional<BookDTO> buildBookDTO(String bookJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(bookJson);
            // The isbnField gets here like this: ISBN:XXXX, where XXXX is the isbn number
            String isbnValue = actualObj.fieldNames().next().replaceAll("ISBN:", "");
            JsonNode titleNode = actualObj.findPath(TITLE_FIELD);
            JsonNode subtitleNode = actualObj.findPath(SUBTITLE_FIELD);
            JsonNode imageField = actualObj.findPath(COVER_FIELD);
            JsonNode publishDateNode = actualObj.findPath(PUBLISH_DATE_FIELD);
            JsonNode pagesNode = actualObj.findPath(PAGES_FIELD);
            JsonNode publishersNode = actualObj.findPath(PUBLISHER_FIELD);
            JsonNode authorsNode = actualObj.findPath(AUTHORS_FIELD);
            if (titleNode.isMissingNode() || subtitleNode.isMissingNode() || publishDateNode
                .isMissingNode() || pagesNode.isMissingNode() || publishersNode.isMissingNode()
                || authorsNode.isMissingNode() || imageField.isMissingNode()) {
                throw new RequiredFieldNotExists(
                    "Some of the required fields of the book are not complete");
            } else {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setIsbn(isbnValue);
                bookDTO.setTitle(titleNode.textValue());
                bookDTO.setSubtitle(subtitleNode.textValue());
                bookDTO.setYear(publishDateNode.textValue());
                bookDTO.setPages(pagesNode.asInt());
                bookDTO.setImage(imageField.findPath(COVER_SIZE_FIELD).textValue());
                bookDTO.setPublisher(publishersNode.findPath(NAME_FIELD).textValue());
                bookDTO.setAuthor(authorsNode.findPath(NAME_FIELD).textValue());
                return Optional.of(bookDTO);
            }
        } catch (IOException | NoSuchElementException ex) {
            return Optional.empty();
        }
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
