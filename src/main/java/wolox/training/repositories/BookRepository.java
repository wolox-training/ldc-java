package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findByAuthorIgnoreCase(String author);

    Optional<Book> findByIsbn(String isbn);

    @Query(
        "SELECT b FROM Book b WHERE (b.publisher = :publisher OR :publisher IS NULL) AND "
            + "(b.genre = :genre OR :genre IS NULL) AND (b.year = :year OR :year IS NULL)")
    Optional<List<Book>> findAllByPublisherAndGenreAndYear(@Param("publisher") String publisher,
        @Param("genre") String genre, @Param("year") String year);

}
