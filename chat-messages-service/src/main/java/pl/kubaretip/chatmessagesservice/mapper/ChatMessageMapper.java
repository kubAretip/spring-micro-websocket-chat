package pl.kubaretip.chatmessagesservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kubaretip.chatmessagesservice.constant.DateConstants;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.dtomodels.ChatMessageDTO;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "time", expression = "java(convertDate(chatMessageDTO.getTime()))")
    ChatMessage mapToChatMessage(ChatMessageDTO chatMessageDTO);

    default Date convertDate(String timeInString) {
        var localDateTime = LocalDateTime.parse(timeInString, DateTimeFormatter.ofPattern(DateConstants.UTC_DATE_FORMAT));
        return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
    }


}
