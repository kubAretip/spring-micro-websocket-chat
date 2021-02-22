package pl.kubaretip.chatmessagesservice.service;

import pl.kubaretip.chatmessagesservice.document.ChatMessage;
import pl.kubaretip.dtomodels.ChatMessageDTO;
import reactor.core.publisher.Mono;

public interface ChatMessageService {
    Mono<ChatMessage> saveChatMessage(ChatMessageDTO chatMessageDTO);
}
