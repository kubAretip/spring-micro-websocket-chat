package pl.kubaretip.chatmessagesservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.kubaretip.chatmessagesservice.constant.DateConstants;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.dtomodels.ChatMessageDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {

    @Mapping(target = "time", expression = "java(convertDate(chatMessageDTO.getTime()))")
    @Mapping(target = "status" , ignore = true)
    ChatMessage mapToChatMessage(ChatMessageDTO chatMessageDTO);

    default Date convertDate(String timeInString) {
        var formatter = new SimpleDateFormat(DateConstants.MESSAGE_DATE_FORMAT, DateConstants.MESSAGE_DATE_LOCALE);
        try {
            return formatter.parse(timeInString);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid message date format");
        }
    }


}
