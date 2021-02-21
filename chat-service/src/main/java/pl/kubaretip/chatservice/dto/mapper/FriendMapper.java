package pl.kubaretip.chatservice.dto.mapper;

import org.mapstruct.*;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.Friend;
import pl.kubaretip.chatservice.dto.ChatProfileDTO;
import pl.kubaretip.chatservice.dto.FriendDTO;
import pl.kubaretip.chatservice.util.DateUtils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Mapper(componentModel = "spring", uses = ChatProfileMapper.class)
public interface FriendMapper {

    @Mapping(target = "sender", qualifiedByName = "chatProfileToChatProfileDTO")
    @Mapping(target = "recipient", qualifiedByName = "chatProfileToChatProfileDTO")
    @Mapping(target = "sentTime", expression = "java(convertOffsetDateToString(friend.getSentTime()))")
    FriendDTO mapToFriendDTO(Friend friend);

    @Named("mapWithoutRecipient")
    @InheritConfiguration(name = "mapToFriendDTO")
    @Mapping(target = "recipient", ignore = true)
    FriendDTO mapToFriendDTOWithoutRecipient(Friend friend);

    @IterableMapping(qualifiedByName = "mapWithoutRecipient")
    List<FriendDTO> mapToFriendDTOListWithoutRecipient(List<Friend> friends);


    default String convertOffsetDateToString(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN));
    }

}
