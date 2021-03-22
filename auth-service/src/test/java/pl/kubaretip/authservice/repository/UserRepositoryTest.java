package pl.kubaretip.authservice.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Sql(scripts = {"/db/migration/V1.0.4__insert_authority_records.sql", "/db/import_users.sql"})
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void shouldReturnUserWithAuthorities() {

        // given + when
        var user = userRepository.findOneWithAuthoritiesByUsernameIgnoreCase("fake_user");

        // then
        assertTrue(user.isPresent());
        assertThat(user.get().getUsername(), is(equalTo("fake_user")));
        assertThat(user.get().getAuthorities(), hasSize(1));
        assertThat(user.get().getAuthorities().iterator().next().getName(), is(equalToIgnoringCase("role_user")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"fake_user", "FAKE_USER", "FaKE_UsEr"})
    public void shouldReturnTrueWhenUserExists(String username) {
        // given + when + then
        assertTrue(userRepository.existsByUsernameIgnoreCase(username));
    }

    @Test
    public void shouldReturnFalseWhenUserNotExists() {
        // given + when + then
        assertFalse(userRepository.existsByUsernameIgnoreCase("not_exists"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"fake_mail", "FAKE_MAIL", "faKE_maiL"})
    public void shouldReturnTrueWhenExistsUserWithEmail(String mail) {
        // given + when + then
        assertTrue(userRepository.existsByEmailIgnoreCase(mail));
    }

    @Test
    public void shouldReturnUserByActivationKey() {

        // given + when
        var user = userRepository.findOneByActivationKey("fake_key");

        // then
        assertTrue(user.isPresent());
        assertThat(user.get().getActivationKey(),is(equalTo("fake_key")));
    }


}
