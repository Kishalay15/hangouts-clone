package hangoutsclone.hangouts.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.UpdateUserRequest;
import hangoutsclone.hangouts.response.ApiResponse;
import hangoutsclone.hangouts.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) throws UserException {

        User user = userService.findUserProfile(token);
        
        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{query}")
    public ResponseEntity<List<User>> searchUser(@PathVariable String query) {
        List<User> users = userService.searchUser(query);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> updateUserProfile(@RequestBody UpdateUserRequest request, @RequestHeader("Authorization") String token) throws UserException {
        User user = userService.findUserProfile(token);

        userService.updateUser(user.getId(), request);
        ApiResponse response = new ApiResponse("User updated successfully", true);

        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}
