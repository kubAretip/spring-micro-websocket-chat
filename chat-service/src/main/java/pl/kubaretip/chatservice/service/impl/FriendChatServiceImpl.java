package pl.kubaretip.chatservice.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kubaretip.chatservice.domain.ChatProfile;
import pl.kubaretip.chatservice.domain.FriendChat;
import pl.kubaretip.chatservice.exception.AlreadyExistsException;
import pl.kubaretip.chatservice.repository.FriendChatRepository;
import pl.kubaretip.chatservice.service.FriendChatService;

import javax.transaction.Transactional;

@Slf4j
@Service
public class FriendChatServiceImpl implements FriendChatService {

    private final FriendChatRepository friendChatRepository;

    public FriendChatServiceImpl(FriendChatRepository friendChatRepository) {
        this.friendChatRepository = friendChatRepository;
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


}
