package pl.kubaretip.chatservice.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.dto.ChatProfileDTO;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChatProfileMapper {

    @Named("chatProfileToChatProfileDTO")
    @Mapping(target = "userId", expression = "java(convertUUIDtoString(chatProfile.getUserId()))")
    ChatProfileDTO chatProfileToChatProfileDTO(ChatProfile chatProfile);

    default String convertUUIDtoString(UUID id) {
        return id.toString();
    }

}
