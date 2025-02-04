package hangoutsclone.hangouts.services;

import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.requests.UpdateUserRequest;

public interface UserService {
    
    public User findUserById(Integer id) throws UserException;
    public User findUserProfile(String jwt) throws UserException;
    public User updateUser(Integer id, UpdateUserRequest updateUserRequest) throws UserException;
}
