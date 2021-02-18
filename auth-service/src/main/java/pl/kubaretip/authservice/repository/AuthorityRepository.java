package pl.kubaretip.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kubaretip.authservice.domain.Authority;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Optional<Authority> findByNameIgnoreCase(String name);


}
