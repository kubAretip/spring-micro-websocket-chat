package pl.kubaretip.authservice.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.kubaretip.authservice.domain.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserMapperImpl.class})
public class UserMapperImplTest {


    @Autowired
    private UserMapper userMapper;

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
