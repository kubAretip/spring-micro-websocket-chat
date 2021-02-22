package pl.kubaretip.chatservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendChat;
import pl.kubaretip.chatservice.exception.AlreadyExistsException;
import pl.kubaretip.chatservice.exception.NotFoundException;
import pl.kubaretip.chatservice.repository.ChatProfileRepository;
import pl.kubaretip.chatservice.repository.FriendChatRepository;
import pl.kubaretip.chatservice.service.FriendChatService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FriendChatServiceImpl implements FriendChatService {

    private final FriendChatRepository friendChatRepository;
    private final ChatProfileRepository chatProfileRepository;

    public FriendChatServiceImpl(FriendChatRepository friendChatRepository,
                                 ChatProfileRepository chatProfileRepository) {
        this.friendChatRepository = friendChatRepository;
        this.chatProfileRepository = chatProfileRepository;
    }

    @Transactional
    @Override
    public void createFriendChat(ChatProfile firstUserChatProfile, ChatProfile secondUserChatProfile) {

        if (friendChatRepository.existsFriendChatForUsers(firstUserChatProfile, secondUserChatProfile)) {
            throw new AlreadyExistsException("Chat for users already exists");
        }

        var friendChatForFirstUser = new FriendChat();
        var friendChatForSecondUser = new FriendChat();

        friendChatForFirstUser.setSender(firstUserChatProfile);
        friendChatForFirstUser.setRecipient(secondUserChatProfile);

        friendChatForSecondUser.setSender(secondUserChatProfile);
        friendChatForSecondUser.setRecipient(firstUserChatProfile);
        friendChatRepository.save(friendChatForFirstUser);
        friendChatRepository.save(friendChatForSecondUser);

        friendChatForFirstUser.setChatWith(friendChatForSecondUser);
        friendChatForSecondUser.setChatWith(friendChatForFirstUser);

        friendChatRepository.save(friendChatForFirstUser);
        friendChatRepository.save(friendChatForSecondUser);

    }

    @Override
    public List<FriendChat> getAllFriendsChatsBySender(String currentUserId) {
        return chatProfileRepository.findById(UUID.fromString(currentUserId))
                .map(friendChatRepository::findBySender)
                .orElseThrow(() -> new NotFoundException("User with id " + currentUserId + " not found"));
    }
}
