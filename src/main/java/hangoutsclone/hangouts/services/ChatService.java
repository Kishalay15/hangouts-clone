package hangoutsclone.hangouts.services;

import java.util.List;

import hangoutsclone.hangouts.entities.Chat;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.GroupChatRequest;

public interface ChatService {

    public Chat createChat(User currUser, Integer otherUser) throws UserException;

    public Chat findChatById(Integer chatId) throws ChatException;

    public List<Chat> findChatsByUserID(Integer userId) throws UserException;

    public Chat createGroupChat(GroupChatRequest request, User currUser) throws UserException;

    public Chat addUserToGroupChat(Integer chatId, Integer userId, User currUser) throws UserException, ChatException;

    public Chat renameGroupChat(Integer chatId, String groupName, User currUser) throws ChatException, UserException;

    public Chat removeUserFromGroupChat(Integer chatId, Integer userId, User currUser) throws ChatException, UserException;

    public void deleteChat(Integer chatId, Integer currUser) throws ChatException, UserException;
}
