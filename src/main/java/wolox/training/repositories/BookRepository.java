package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.model.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByAuthorIgnoreCase(String author);

}
