package wolox.training;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import wolox.training.config.SpringSecurityInitializer;
import wolox.training.config.WebAppInitializer;
import wolox.training.config.WebMvcConfig;
import wolox.training.controllers.BookController;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.BookService;
import wolox.training.services.CustomUserDetailsService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebMvcConfig.class, SpringSecurityInitializer.class, WebAppInitializer.class})
@WebAppConfiguration
public class BookControllerTest {

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

    private Book oneTestBook;

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.wac).apply(
            SecurityMockMvcConfigurers.springSecurity()).build();
        this.mockBookRepository = mock(BookRepository.class);
        this.mockUserRepository = mock(UserRepository.class);
        this.mockBookService = mock(BookService.class);
        this.mockCustomUserDetailsService = mock(CustomUserDetailsService.class);
        BookController.setBookRepository(mockBookRepository);
        BookController.setBookService(mockBookService);
        oneTestBook = new Book();
        oneTestBook.setAuthor("Carlitos");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("unaImagen");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(259);
        oneTestBook.setPublisher("LaGuitarra");
        oneTestBook.setSubtitle("CarlitosWay");
        oneTestBook.setTitle("Las aventuras terrorificas de Carlitos");
        oneTestBook.setYear("2005");
    }

    @Test
    public void whenPostBook_thenBookIsCreated() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);

        mvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void whenFindByIdWhichNotExists_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void whenDeleteNotExistingBook_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test(expected = NestedServletException.class)
    public void whenPutBookWithDifferentId_thenBookIdMismatchException() throws Exception {
        String url = ("/api/books/100");
        String newYear = "2010";
        String newTitle = "Las NUEVAS aventuras terrorificas de Carlitos";
        oneTestBook.setTitle(newTitle);
        oneTestBook.setYear(newYear);
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
        String url = ("/api/books/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenDeleteWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenPutWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/books/100");
        String newYear = "2010";
        String newTitle = "Las NUEVAS aventuras terrorificas de Carlitos";
        oneTestBook.setTitle(newTitle);
        oneTestBook.setYear(newYear);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    public void whenFindByEveryFieldAndExists_thenReturnOk() throws Exception {
        Pageable customPageable = new PageRequest(0, 5, new Sort("title"));
        List<Book> books = new ArrayList<Book>();
        books.add(oneTestBook);
        when(mockBookRepository.findAllByEveryField("1", "Terror", "Carlitos",
            "unaImagen", "Las aventuras terrorificas de Carlitos",
            "CarlitosWay", "LaGuitarra", "2005", "2005",
            "259", "4578-8665", customPageable)).thenReturn(new PageImpl<Book>(books));
        String url = ("/api/books?id=1&genre=Terror&author=Carlitos&image=unaImagen&"
            + "title=Las aventuras terrorificas de Carlitos&subtitle=CarlitosWay&"
            + "publisher=LaGuitarra&fromYear=2005&toYear=2005&pages=259&isbn=4578-8665&"
            + "page=0&size=5&sort=title");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void whenFindAndNotExists_thenReturnNotFound() throws Exception {
        Pageable customPageable = new PageRequest(0, 5, new Sort("title"));
        List<Book> books = new ArrayList<Book>();
        books.add(oneTestBook);
        when(mockBookRepository.findAllByEveryField("1", "Terror", "Carlitos",
            "unaImagen", "Las aventuras terrorificas de Carlitos",
            "CarlitosWay", "LaGuitarra", "2005", "2005",
            "259", "4578-8665", customPageable)).thenReturn(new PageImpl(books));
        String url = ("/api/books?genre=Comedia&author=Carlitos");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}
