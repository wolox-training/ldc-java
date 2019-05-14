package wolox.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import wolox.training.model.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User oneTestUser;

    @Before
    public void setUp() {
        Book oneTestBook = new Book();
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
        oneTestUser = new User();
        oneTestUser.setUsername("carlitosbala");
        oneTestUser.setPassword("pass");
        oneTestUser.setName("Carlos Rovira");
        oneTestUser.setBirthdate(LocalDate.parse("2000-03-27"));
        oneTestUser.addBook(oneTestBook);
        userRepository.save(oneTestUser);
    }

    @Test
    public void whenCreateUser_thenUserIsPersisted() {
        User persistedUser = userRepository.findFirstByUsername("carlitosbala").orElse(null);
        assertThat(persistedUser.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(persistedUser.getName().equals(oneTestUser.getName()));
        assertThat(persistedUser.getBirthdate().equals(oneTestUser.getBirthdate()));
        assertThat(persistedUser.getBooks().equals(oneTestUser.getBooks()));
        assertThat(persistedUser.getPassword().equals(oneTestUser.getPassword()));
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutUsername_thenThrowException() {
        oneTestUser.setUsername(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutName_thenThrowException() {
        oneTestUser.setName(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutBirthdate_thenThrowException() {
        oneTestUser.setBirthdate(null);
        userRepository.save(oneTestUser);
    }

    @Test
    public void whenFindByBirthdateAndName_thenUserIsReturned() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "Rovi").get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

}
