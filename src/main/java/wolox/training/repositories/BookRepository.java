package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByAuthorIgnoreCase(String author);

}
