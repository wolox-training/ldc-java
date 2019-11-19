package wolox.training;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import wolox.training.config.WebMvcConfig;
import wolox.training.controllers.UserController;
import wolox.training.models.Book;
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.BookService;
import wolox.training.services.CustomUserDetailsService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = WebMvcConfig.class)
@WebAppConfiguration
public class UserControllerTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mvc;

    @SuppressWarnings("unused")
    private BookRepository mockBookRepository;

    @SuppressWarnings("unused")
    private UserRepository mockUserRepository;

    @SuppressWarnings("unused")
    private BookService mockBookService;

    @SuppressWarnings("unused")
    private CustomUserDetailsService mockCustomUserDetailsService;

    private User oneTestUser;
    private Book oneTestBook;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(
            SecurityMockMvcConfigurers.springSecurity()).build();
        this.mockBookRepository = mock(BookRepository.class);
        this.mockUserRepository = mock(UserRepository.class);
        this.mockBookService = mock(BookService.class);
        this.mockCustomUserDetailsService = mock(CustomUserDetailsService.class);
        UserController.setBookRepository(mockBookRepository);
        UserController.setUserRepository(mockUserRepository);
        oneTestBook = new Book();
        oneTestBook.setAuthor("Stephen King");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("https://imagesforus.com/1dsafr12.png");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(1502);
        oneTestBook.setPublisher("Viking Press");
        oneTestBook.setSubtitle("Worst clown ever");
        oneTestBook.setTitle("It");
        oneTestBook.setYear("1986");
        oneTestUser = new User();
        oneTestUser.setUsername("ramiselton");
        oneTestUser.setName("Ramiro Selton");
        oneTestUser.setBirthdate(LocalDate.parse("1990-03-27"));
        oneTestUser.addBook(oneTestBook);
    }

    @WithMockUser
    @Test
    public void whenFindByIdWhichExists_thenUserIsReturned() throws Exception {
        Mockito.when(mockUserRepository.findOne(1L)).thenReturn(oneTestUser);
        String url = ("/api/users/1");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(
                "{\"id\":0,\"username\":\"ramiselton\",\"name\":\"Ramiro Selton\","
                    + "\"birthdate\":\"1990-03-27\",\"books\":[{\"id\":0,\"genre\":\"Terror\","
                    + "\"author\":\"Stephen King\",\"image\":\"https://imagesforus.com/1dsafr12.png\""
                    + ",\"title\":\"It\",\"subtitle\":\"Worst clown ever\",\"publisher\":"
                    + "\"Viking Press\",\"year\":\"1986\",\"pages\":1502,\"isbn\":\"4578-8665\","
                    + "\"users\":null}]}"
            ));
    }

    @WithMockUser
    @Test
    public void whenFindByIdWhichNotExists_thenNotFound() throws Exception {
        String url = ("/api/users/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void whenDeleteNotExistingBook_thenNotFound() throws Exception {
        String url = ("/api/users/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test(expected = NestedServletException.class)
    public void whenPutBookWithDifferentId_thenBookIdMismatchException() throws Exception {
        String url = ("/api/users/100");
        String newUsername = "newUsername";
        oneTestUser.setUsername(newUsername);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/users/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenDeleteWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/users/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenPutWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/users/100");
        String newUsername = "newUsername";
        oneTestUser.setUsername(newUsername);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isUnauthorized());
    }

}
