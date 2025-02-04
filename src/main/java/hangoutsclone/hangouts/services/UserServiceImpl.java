package hangoutsclone.hangouts.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;

import hangoutsclone.hangouts.config.TokenProvider;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.repository.UserRepo;
import hangoutsclone.hangouts.requests.UpdateUserRequest;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    private TokenProvider tokenProvider;

    @Override
    public User findUserById(Integer id) throws UserException {
        Optional<User> optionalUser = userRepo.findById(id);

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public User findUserProfile(String jwt) throws UserException {
        String email = tokenProvider.getEmailFromToken(jwt);

        if (email == null) {
            throw new BadCredentialsException("Invalid token");
        }

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UserException("User not found");
        }
        return user;
    }

    @Override
    public User updateUser(Integer id, UpdateUserRequest updateUserRequest) throws UserException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateUser'");
    }
    
}
