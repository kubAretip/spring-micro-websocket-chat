package pl.kubaretip.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaretip.authservice.domain.User;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneWithAuthoritiesByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findOneByActivationKey(String activationKey);

}
