package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import wolox.training.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findFirstByUsername(String userName);

}
