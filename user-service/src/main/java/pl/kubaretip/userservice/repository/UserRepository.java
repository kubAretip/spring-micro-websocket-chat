package pl.kubaretip.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaretip.userservice.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByUsernameIgnoreCase(String username);

    Optional<User> findOneByEmailIgnoreCase(String email);

}
