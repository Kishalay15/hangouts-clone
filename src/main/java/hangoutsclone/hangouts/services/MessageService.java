package hangoutsclone.hangouts.services;

import java.util.List;

import hangoutsclone.hangouts.entities.Message;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.MessageException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.SendMessageRequest;

public interface MessageService {

    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException;

    public List<Message> getChatsMessages(Integer chatId, User requestingUser) throws ChatException, UserException;

    public Message findMessageById(Integer messageId) throws MessageException;

    public void deleteMessage(Integer messageId, User requestingUser) throws MessageException, UserException;
}
