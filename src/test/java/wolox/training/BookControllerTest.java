package wolox.training;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.controller.BookController;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;


    @Test
    public void whenPostBook_thenBookIsPersisted() {

    }

    @Test
    public void whenFindByIdWhichExists_thenReturnBook() {

    }

    @Test(expected = ResponseStatusException.class)
    public void whenFindByIdWhichNotExists_thenThrowException() {

    }


}
