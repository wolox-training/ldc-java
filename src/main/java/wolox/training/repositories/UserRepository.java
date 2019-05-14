package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByUsername(String userName);

    Optional<User> findAllByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate dateStart,
        LocalDate dateEnd, String name);

}
