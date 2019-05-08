package wolox.training.repositories;

import org.springframework.data.repository.CrudRepository;
import wolox.training.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findFirstByUsername(String userName);

}
