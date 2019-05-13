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
    private final static String PUBLISHER_FIELD = "publishers";
    private final static String NAME_FIELD = "name";
    private final static String PUBLISH_DATE_FIELD = "publish_date";
    private final static String PAGES_FIELD = "number_of_pages";
    private final static String AUTHORS_FIELD = "authors";
    private String isbn;
    private String title;
    private String subtitle;
    private String publisher;
    private String publishDate;
    private Integer pages;
    private String authors;

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
            JsonNode publishDateNode = actualObj.findPath(PUBLISH_DATE_FIELD);
            JsonNode pagesNode = actualObj.findPath(PAGES_FIELD);
            JsonNode publishersNode = actualObj.findPath(PUBLISHER_FIELD);
            JsonNode authorsNode = actualObj.findPath(AUTHORS_FIELD);
            if (titleNode.isMissingNode() || subtitleNode.isMissingNode() || publishDateNode
                .isMissingNode() || pagesNode.isMissingNode() || publishersNode.isMissingNode()
                || authorsNode.isMissingNode()) {
                throw new RequiredFieldNotExists(
                    "Some of the required fields of the book are not complete");
            } else {
                BookDTO bookDTO = new BookDTO();
                bookDTO.setIsbn(isbnValue);
                bookDTO.setTitle(titleNode.textValue());
                bookDTO.setSubtitle(subtitleNode.textValue());
                bookDTO.setPublishDate(publishDateNode.textValue());
                bookDTO.setPages(pagesNode.asInt());
                bookDTO.setPublisher(publishersNode.findPath(NAME_FIELD).textValue());
                bookDTO.setAuthors(authorsNode.findPath(NAME_FIELD).textValue());
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

}
