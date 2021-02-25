package pl.kubaretip.authservice.mapper;

import org.mapstruct.InheritConfiguration;
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

    @InheritConfiguration(name = "mapToUserDTO")
    @Mapping(target = "activationKey", ignore = true)
    UserDTO mapToUserDTOWithoutActivationKey(User user);

    default String convertIdToString(UUID id) {
        if (id == null) {
            return null;
        }
        return id.toString();
    }


}
