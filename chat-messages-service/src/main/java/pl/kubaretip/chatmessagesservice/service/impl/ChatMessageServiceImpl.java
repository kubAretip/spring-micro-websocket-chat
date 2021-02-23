package pl.kubaretip.chatmessagesservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.kubaretip.chatmessagesservice.constant.DateConstants;
import pl.kubaretip.chatmessagesservice.constant.MessageStatus;
import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.chatmessagesservice.mapper.ChatMessageMapper;
import pl.kubaretip.chatmessagesservice.repository.ChatMessageRepository;
import pl.kubaretip.chatmessagesservice.service.ChatMessageService;
import pl.kubaretip.dtomodels.ChatMessageDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
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

    @Override
    public Flux<ChatMessage> findLastUsersMessagesFromTime(long firstUserFriendChatId, long secondUserFriendChatId,
                                                                String beforeTime, int numberOfMessagesToFetch) {

        return Flux.just(beforeTime)
                .flatMap(beforeTimeInString -> {
                    var localDateTime = LocalDateTime.parse(beforeTime, DateTimeFormatter.ofPattern(DateConstants.UTC_DATE_FORMAT));
                    return Mono.just(Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
                })
                .flatMap(formattedDate -> {
                            var sortedByTimeDescWithSize = PageRequest.of(0, numberOfMessagesToFetch, Sort.by("time").descending());
                            return chatMessageRepository
                                    .findByTimeLessThanAndFriendChatIn(
                                            formattedDate,
                                            List.of(firstUserFriendChatId, secondUserFriendChatId),
                                            sortedByTimeDescWithSize);
                        }
                );
    }


    @Override
    public Flux<ChatMessage> getLastUserMessages(long friendChatId1, long friendChatId2, int numberOfMessagesToFetch) {
        var sortedByTimeDescWithSize = PageRequest.of(0, numberOfMessagesToFetch, Sort.by("time").descending());
        return chatMessageRepository.findByFriendChatOrFriendChat(friendChatId1, friendChatId2, sortedByTimeDescWithSize);
    }
}
