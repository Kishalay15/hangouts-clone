package hangoutsclone.hangouts.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangoutsclone.hangouts.entities.Chat;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.ChatException;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.GroupChatRequest;
import hangoutsclone.hangouts.requests.SingleChatRequest;
import hangoutsclone.hangouts.response.ApiResponse;
import hangoutsclone.hangouts.services.ChatService;
import hangoutsclone.hangouts.services.UserService;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private ChatService chatService;
    private UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @PostMapping("/single")
    public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singleChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        User requestingUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createChat(requestingUser, singleChatRequest.getUserId());

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PostMapping("/group")
    public ResponseEntity<Chat> createGroupHandler(@RequestBody GroupChatRequest groupChatRequest, @RequestHeader("Authorization") String jwt) throws UserException {
        User requestingUser = userService.findUserProfile(jwt);
        Chat chat = chatService.createGroupChat(groupChatRequest, requestingUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<Chat> findChatByIdHandler(@PathVariable Integer chatId ,@RequestHeader("Authorization") String jwt) throws ChatException {
        Chat chat = chatService.findChatById(chatId);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Chat>> getAllChatsHandler(@RequestHeader("Authorization") String jwt) throws UserException {
        User requestingUser = userService.findUserProfile(jwt);
        List<Chat> chats = chatService.findChatsByUserID(requestingUser.getId());

        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ResponseEntity<Chat> addUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User requestingUser = userService.findUserProfile(jwt);
        Chat chat = chatService.addUserToGroupChat(chatId, userId, requestingUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ResponseEntity<Chat> removeUserToGroupHandler(@PathVariable Integer chatId, @PathVariable Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User requestingUser = userService.findUserProfile(jwt);
        Chat chat = chatService.removeUserFromGroupChat(chatId, userId, requestingUser);

        return new ResponseEntity<>(chat, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<ApiResponse> deleteGroupHandler(@PathVariable Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException {
        User user = userService.findUserProfile(jwt);
        chatService.deleteChat(chatId, user.getId());

        ApiResponse res = new ApiResponse("Chat was deleted", true);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
