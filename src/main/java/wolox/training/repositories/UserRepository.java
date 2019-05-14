package wolox.training.repositories;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import wolox.training.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByUsername(String userName);

    Optional<User> findAllByBirthdateBetweenAndNameContainingIgnoreCase(LocalDate timeStart,
        LocalDate timeEnd, String name);

}
