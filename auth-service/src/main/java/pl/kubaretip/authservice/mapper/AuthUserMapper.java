package pl.kubaretip.authservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kubaretip.authservice.domain.AuthUser;
import pl.kubaretip.dtomodels.AuthUserDTO;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {

    @Mapping(target = "password", ignore = true)
    AuthUserDTO mapToAuthUserDTO(AuthUser authUser);


}
