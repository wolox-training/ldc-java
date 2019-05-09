package wolox.training;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.model.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Book oneTestBook;

    @Before
    public void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Carlitos");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("unaImagen");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(259);
        oneTestBook.setPublisher("LaGuitarra");
        oneTestBook.setSubtitle("Carlito's Way");
        oneTestBook.setTitle("Las aventuras terrorificas de Carlitos");
        oneTestBook.setYear("2005");
        bookRepository.save(oneTestBook);
    }

    @Test
    public void whenFindBookByAuthorAndExists_thenBookIsReturned() {
        Book bookFromDatabase = bookRepository.findByAuthorIgnoreCase("Carlitos").orElseGet(null);
        assertThat(bookFromDatabase).isEqualToComparingFieldByField(oneTestBook);
    }

    @Test
    public void whenCreateBook_thenBookIsPersisted() {
        Book persistedBook = bookRepository.findByAuthorIgnoreCase("Carlitos").orElse(null);
        assertThat(persistedBook.getAuthor().equals(oneTestBook.getAuthor()));
        assertThat(persistedBook.getGenre().equals(oneTestBook.getGenre()));
        assertThat(persistedBook.getImage().equals(oneTestBook.getImage()));
        assertThat(persistedBook.getIsbn().equals(oneTestBook.getIsbn()));
        assertThat(persistedBook.getPages().equals(oneTestBook.getPages()));
        assertThat(persistedBook.getPublisher().equals(oneTestBook.getPublisher()));
        assertThat(persistedBook.getSubtitle().equals(oneTestBook.getSubtitle()));
        assertThat(persistedBook.getTitle().equals(oneTestBook.getTitle()));
        assertThat(persistedBook.getYear().equals(oneTestBook.getYear()));
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutAuthor_thenThrowException() {
        oneTestBook.setAuthor(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutImage_thenThrowException() {
        oneTestBook.setImage(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutTitle_thenThrowException() {
        oneTestBook.setTitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutSubtitle_thenThrowException() {
        oneTestBook.setSubtitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutPublisher_thenThrowException() {
        oneTestBook.setPublisher(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutYear_thenThrowException() {
        oneTestBook.setYear(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutPages_thenThrowException() {
        oneTestBook.setPages(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateBookWithoutIsbn_thenThrowException() {
        oneTestBook.setIsbn(null);
        bookRepository.save(oneTestBook);
    }

}
