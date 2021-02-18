package pl.kubaretip.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaretip.authservice.domain.AuthUser;

import java.util.Optional;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    Optional<AuthUser> findOneWithAuthoritiesByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);

}
