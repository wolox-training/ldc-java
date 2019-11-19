package wolox.training;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import wolox.training.config.WebMvcConfig;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
@WebAppConfiguration
@Transactional
@ActiveProfiles
public class UserRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private UserRepository userRepository;

    @Autowired
    @SuppressWarnings("unused")
    private BookRepository bookRepository;

    private User oneTestUser;
    private User anotherTestUser;

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
        anotherTestUser = new User();
        anotherTestUser.setUsername("aleblanco");
        anotherTestUser.setPassword("pass");
        anotherTestUser.setName("Alejandro Blanco");
        anotherTestUser.setBirthdate(LocalDate.parse("1990-03-27"));
        anotherTestUser.addBook(oneTestBook);
        userRepository.save(anotherTestUser);
    }

    @Test
    public void whenCreateUser_thenUserIsPersisted() {
        User persistedUser = userRepository.findFirstByUsername("carlitosbala");
        if (persistedUser == null) {
          persistedUser = new User();
        }
        assertThat(persistedUser.getUsername()
            .equals(oneTestUser.getUsername())).isTrue();
        assertThat(persistedUser.getName()
            .equals(oneTestUser.getName())).isTrue();
        assertThat(persistedUser.getBirthdate()
            .equals(oneTestUser.getBirthdate())).isTrue();
        assertThat(persistedUser.getBooks().size() == oneTestUser.getBooks().size())
            .isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithoutUsername_thenThrowException() {
        oneTestUser.setUsername(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = IllegalArgumentException.class)
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
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        Page<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "Rovi",
                customPageable);
        User user = users.getContent().get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByBirthdateAndNull_thenUserIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable).getContent();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByNameAndNull_thenUserIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("0000-01-01", formatter);
        LocalDate to = LocalDate.parse("9999-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "Rovi", customPageable).getContent();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByFromAndDefaultValues_thenUserIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("9999-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "", customPageable).getContent();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByToAndDefaultValues_thenUserIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate to = LocalDate.parse("2005-12-01", formatter);
        LocalDate from = LocalDate.parse("0000-01-01", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "", customPageable).getContent();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenSortElements_thenCorrectOrderedListIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable).getContent();
        User firstUser = users.get(0);
        User secondUser = users.get(1);
        assertThat(firstUser.getUsername().equals(anotherTestUser.getUsername())).isTrue();
        assertThat(secondUser.getUsername().equals(oneTestUser.getUsername())).isTrue();
    }

    @Test
    public void whenChangePageElementsNumber_thenCorrectSizedListIsReturned() {
        Pageable customPageable = new PageRequest(0, 5, new Sort("id"));
        DateTimeFormatter formatter = ISODateTimeFormat.date();
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable).getContent();
        assertThat(users.size() == 1).isTrue();
    }

}
