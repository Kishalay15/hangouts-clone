package hangoutsclone.hangouts.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangoutsclone.hangouts.entities.Message;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.MessageException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.SendMessageRequest;
import hangoutsclone.hangouts.response.ApiResponse;
import hangoutsclone.hangouts.services.MessageService;
import hangoutsclone.hangouts.services.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;
    private UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest request, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = userService.findUserProfile(jwt);
        request.setUserId(user.getId());
        Message message = messageService.sendMessage(request);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getChatMessagesHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {

        User user = userService.findUserProfile(jwt);
        List<Message> messages = messageService.getChatsMessages(chatId, user);

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<ApiResponse> deleteMessagesHandler(@PathVariable Integer messageId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException {

        User user = userService.findUserProfile(jwt);
        messageService.deleteMessage(messageId, user);

        ApiResponse response = new ApiResponse("Message deleted succesfully", false);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
