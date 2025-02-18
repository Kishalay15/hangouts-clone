package hangoutsclone.hangouts.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import hangoutsclone.hangouts.entities.Chat;
import hangoutsclone.hangouts.entities.Message;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.MessageException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.repository.MessageRepo;
import hangoutsclone.hangouts.requests.SendMessageRequest;

@Service
public class MessageServiceImpl implements MessageService {

    private MessageRepo messageRepo;
    private UserService userService;
    private ChatService chatService;

    public MessageServiceImpl(MessageRepo messageRepo, UserService userService, ChatService chatService) {
        this.messageRepo = messageRepo;
        this.userService = userService;
        this.chatService = chatService;
    }

    @Override
    public Message sendMessage(SendMessageRequest request) throws UserException, ChatException {
        User user = userService.findUserById(request.getUserId());
        Chat chat = chatService.findChatById(request.getChatId());

        Message message = new Message();
        message.setChat(chat);
        message.setSender(user);
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());

        return message;
    }

    @Override
    public List<Message> getChatsMessages(Integer chatId, User requestingUser) throws ChatException, UserException {
        Chat chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(requestingUser)) {
            throw new UserException("User not authorized");
        }

        List<Message> messages = messageRepo.findByChatId(chat.getId());

        return messages;
    }

    @Override
    public Message findMessageById(Integer messageId) throws MessageException {
        Optional<Message> optMessage = messageRepo.findById(messageId);

        if (optMessage.isPresent()) {
            return optMessage.get();
        }

        throw new MessageException("Message not found");
    }

    @Override
    public void deleteMessage(Integer messageId, User requestingUser) throws MessageException, UserException {
        Message message = findMessageById(messageId);

        if (message.getSender().getId().equals(requestingUser.getId())) {
            messageRepo.deleteById(messageId);
        }

        throw new UserException("User not authorized");
    }

}
