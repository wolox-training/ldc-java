package wolox.training;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import wolox.training.controller.BookController;
import wolox.training.model.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.service.BookService;
import wolox.training.service.CustomUserDetailsService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookRepository mockBookRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private BookService mockBookService;

    @MockBean
    private CustomUserDetailsService mockCustomUserDetailsService;

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

    @Test
    public void whenFindByIdWhichNotExists_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(get(url)
            .with(user("spring"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteNotExistingBook_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(delete(url)
            .with(user("spring"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

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
            .with(user("spring"))
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isNotFound());
    }

}
