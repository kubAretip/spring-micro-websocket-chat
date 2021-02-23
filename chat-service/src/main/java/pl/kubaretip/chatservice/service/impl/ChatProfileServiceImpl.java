package pl.kubaretip.chatservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.chatservice.service.ChatProfileService;
import pl.kubaretip.exceptionutils.InvalidDataException;

import java.util.UUID;

@Slf4j
@Service
public class ChatProfileServiceImpl implements ChatProfileService {

    private final ChatProfileRepository chatProfileRepository;

    public ChatProfileServiceImpl(ChatProfileRepository chatProfileRepository) {
        this.chatProfileRepository = chatProfileRepository;
    }

    @Override
    public ChatProfile createChatProfile(String userId, String username) {

        if (!StringUtils.isNotBlank(userId)) {
            throw new InvalidDataException("Chat profile can't be created because user id is empty.");
        }
        if (!StringUtils.isNotBlank(username)) {
            throw new InvalidDataException("Chat profile can't be created because username is empty.");
        }

        var chatProfile = new ChatProfile();
        chatProfile.setUserId(UUID.fromString(userId));
        chatProfile.setFriendsRequestCode(generateFriendRequestCode(username));
        return chatProfileRepository.save(chatProfile);
    }

    private String generateFriendRequestCode(String username) {
        return username.toLowerCase() + "-" + RandomStringUtils.randomNumeric(10);
    }


}
