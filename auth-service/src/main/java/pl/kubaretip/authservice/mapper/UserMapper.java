package pl.kubaretip.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kubaretip.authservice.domain.User;
import pl.kubaretip.dtomodels.UserDTO;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "id", expression = "java(convertIdToString(user.getId()))")
    UserDTO mapToUserDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "activationKey", ignore = true)
    @Mapping(target = "id", expression = "java(convertIdToString(user.getId()))")
    UserDTO mapToUserDTOWithoutActivationKey(User user);

    default String convertIdToString(UUID id) {
        return id.toString();
    }


}
