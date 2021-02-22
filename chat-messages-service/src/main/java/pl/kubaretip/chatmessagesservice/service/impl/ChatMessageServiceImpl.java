package pl.kubaretip.chatmessagesservice.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.kubaretip.chatmessagesservice.constant.MessageStatus;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.chatmessagesservice.mapper.ChatMessageMapper;
import pl.kubaretip.chatmessagesservice.repository.ChatMessageRepository;
import pl.kubaretip.chatmessagesservice.service.ChatMessageService;
import pl.kubaretip.dtomodels.ChatMessageDTO;
import reactor.core.publisher.Mono;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageMapper chatMessageMapper;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository,
                                  ChatMessageMapper chatMessageMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessageMapper = chatMessageMapper;
    }

    @Override
    public Mono<ChatMessage> saveChatMessage(ChatMessageDTO chatMessageDTO) {

        return Mono.just(chatMessageDTO)
                .flatMap(messageDTO -> {

                    if (!StringUtils.isNotEmpty(messageDTO.getContent())) {
                        return Mono.error(new RuntimeException("Can not save empty message"));
                    }

                    if (messageDTO.getFriendChat() == null) {
                        return Mono.error(new RuntimeException("Can not save message with empty friend chat field"));
                    }

                    if (!StringUtils.isNotEmpty(messageDTO.getSender()) || !StringUtils.isNotEmpty(messageDTO.getRecipient())) {
                        return Mono.error(new RuntimeException("Can not save message with empty sender or recipient"));
                    }

                    if (!StringUtils.isNotEmpty(messageDTO.getTime())) {
                        return Mono.error(new RuntimeException("Can not save message with empty date"));
                    }
                    chatMessageDTO.setStatus(MessageStatus.RECEIVED.name());
                    return Mono.just(chatMessageMapper.mapToChatMessage(messageDTO));
                })
                .flatMap(chatMessageRepository::save);
    }


}
