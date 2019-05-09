package wolox.training.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByAuthorIgnoreCase(String author);

}
