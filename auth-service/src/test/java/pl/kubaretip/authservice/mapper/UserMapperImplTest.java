package pl.kubaretip.authservice.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.kubaretip.authservice.domain.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public class UserMapperImplTest {

    private static UserMapperImpl userMapper;

    @BeforeAll
    public static void setup() {
        userMapper = new UserMapperImpl();
    }

    @Test
    public void shouldIgnorePasswordWhenMappingToDTO() {

        // given
        var user = new User();
        user.setPassword("passs");

        // when
        var userDTO = userMapper.mapToUserDTO(user);

        // then
        assertThat(userDTO.getPassword(), nullValue());

    }

    @Test
    public void shouldIgnoreActivationKeyWhenMappingToDTO() {
        // given
        var user = new User();
        user.setActivationKey("123");

        //when
        var userDTO = userMapper.mapToUserDTOWithoutActivationKey(user);

        //then
        assertThat(userDTO.getActivationKey(), nullValue());
    }


}
