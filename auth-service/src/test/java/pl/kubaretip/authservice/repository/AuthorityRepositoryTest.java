package pl.kubaretip.authservice.repository;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {"/db/migration/V1.0.4__insert_authority_records.sql"})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class AuthorityRepositoryTest {

    @Autowired
    private AuthorityRepository authorityRepository;


    @Test
    public void shouldExistsOneAuthority() {
        // given + when + then
        assertThat(authorityRepository.findAll(), Matchers.hasSize(1));
    }


    @ParameterizedTest
    @ValueSource(strings = {"ROLE_USER", "role_user", "RoLE_UsER"})
    public void shouldFindAuthorityByNameIgnoringCaseWhenExists(String roleName) {

        // given (in value source)
        // when
        var authority = authorityRepository.findByNameIgnoreCase(roleName);

        // then
        assertTrue(authority.isPresent());
        assertThat(authority.get().getName(), containsStringIgnoringCase("role_user"));
    }


}
