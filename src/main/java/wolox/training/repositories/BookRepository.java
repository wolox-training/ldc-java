package wolox.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import wolox.training.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Book findByAuthorIgnoreCase(String author);

}
