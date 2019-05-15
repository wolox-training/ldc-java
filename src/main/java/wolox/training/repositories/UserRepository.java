package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByUsername(String userName);

    @Query("SELECT u FROM User u WHERE "
        + " (u.birthdate BETWEEN :dateStart AND :dateEnd)"
        + " OR ( cast(:dateStart as date) IS NULL AND u.birthdate < :dateEnd )"
        + " OR ( cast(:dateEnd as date) IS NULL AND u.birthdate > :dateStart )"
        + " OR (u.name LIKE %:name%)")
    Optional<List<User>> findAllByBirthdateBetweenAndNameContainingIgnoreCase(
        @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd,
        @Param("name") String name, Pageable pageable);

}
