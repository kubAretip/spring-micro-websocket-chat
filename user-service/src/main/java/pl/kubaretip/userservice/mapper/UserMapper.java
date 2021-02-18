package pl.kubaretip.userservice.mapper;

import org.mapstruct.Mapper;

import org.mapstruct.Mapping;
import pl.kubaretip.userservice.domain.User;
import pl.kubaretip.dtomodels.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "password", ignore = true)
    UserDTO mapToUserDTO(User user);

}
