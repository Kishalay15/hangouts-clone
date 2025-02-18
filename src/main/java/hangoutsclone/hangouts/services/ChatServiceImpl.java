package hangoutsclone.hangouts.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hangoutsclone.hangouts.entities.Chat;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.repository.ChatRepo;
import hangoutsclone.hangouts.requests.GroupChatRequest;

@Service
public class ChatServiceImpl implements ChatService {

    private ChatRepo chatRepo;
    private UserService userService;

    public ChatServiceImpl(ChatRepo chatRepo, UserService userService) {
        this.chatRepo = chatRepo;
        this.userService = userService;
    }

    @Override
    public Chat createChat(User currUser, Integer otherUser) throws UserException {
        User user2 = userService.findUserById(otherUser);

        Chat isChatExist = chatRepo.findChatByUser1AndUser2(currUser, user2);

        if (isChatExist != null) {
            return isChatExist;
        }

        Chat newChat = new Chat();
        newChat.setCreatedBy(currUser);
        newChat.getUsers().add(currUser);
        newChat.getUsers().add(user2);
        newChat.setGroup(false);

        return newChat;
    }

    @Override
    public Chat findChatById(Integer chatId) throws ChatException {
        Optional<Chat> chat = chatRepo.findById(chatId);

        if (chat.isPresent()) {
            return chat.get();
        }

        throw new ChatException("Chat not found");
    }

    @Override
    public List<Chat> findChatsByUserID(Integer userId) throws UserException {
        User user = userService.findUserById(userId);
        List<Chat> chats = chatRepo.findByUserId(user.getId());

        return chats;
    }

    @Override
    public Chat createGroupChat(GroupChatRequest request, User currUser) throws UserException {
        Chat group = new Chat();
        group.setGroup(true);
        group.setChat_image(request.getGroupImage());
        group.setChat_name(request.getGroupName());
        group.setCreatedBy(currUser);
        group.getAdmins().add(currUser);

        for (Integer userId:request.getUserIds()) {
            User user = userService.findUserById(userId);
            group.getUsers().add(user);
        }

        return group;
    }

    @Override
    public Chat addUserToGroupChat(Integer chatId, Integer userId, User currUser) throws UserException, ChatException {

        Optional<Chat> optChat = chatRepo.findById(chatId);
        User user = userService.findUserById(userId);

        if (optChat.isPresent()) {
            Chat chat = optChat.get();

            if (chat.getAdmins().contains(currUser)) {
                chat.getUsers().add(user);
                return chatRepo.save(chat);
            } else {
                throw new UserException("User is not admin");
            }
        }

        throw new ChatException("Chat not found");
    }

    @Override
    public Chat renameGroupChat(Integer chatId, String groupName, User currUser) throws ChatException, UserException {
        Optional<Chat> optChat = chatRepo.findById(chatId);

        if (optChat.isPresent()) {
            Chat chat = optChat.get();
            if (chat.getUsers().contains(currUser)) {
                chat.setChat_name(groupName);
                return chatRepo.save(chat);
            }

            throw new UserException("User is not a member of group");
        }

        throw new ChatException("Chat not found");
    }

    @Override
    public Chat removeUserFromGroupChat(Integer chatId, Integer userId, User currUser) throws ChatException, UserException {
        Optional<Chat> optChat = chatRepo.findById(chatId);
        User user = userService.findUserById(userId);

        if (optChat.isPresent()) {
            Chat chat = optChat.get();
            if (chat.getAdmins().contains(currUser)) {
                chat.getUsers().remove(user);
                return chatRepo.save(chat);
            } else if (chat.getUsers().contains(currUser)) {
                if (user.getId().equals(currUser.getId())) {
                    chat.getUsers().remove(user);
                return chatRepo.save(chat);
                }
            }

            throw new UserException("User is not admin");
        }

        throw new ChatException("Chat not found");
    }

    @Override
    public void deleteChat(Integer chatId, Integer currUser) throws ChatException, UserException {
        Optional<Chat> optChat = chatRepo.findById(chatId);
        User user = userService.findUserById(currUser);

        if (optChat.isPresent()) {
            Chat chat = optChat.get();

            chatRepo.deleteById(chatId);
        }
    }

}
