package hangoutsclone.hangouts.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hangoutsclone.hangouts.config.TokenProvider;
import hangoutsclone.hangouts.entities.User;
import hangoutsclone.hangouts.exceptions.UserException;
import hangoutsclone.hangouts.repository.UserRepo;
import hangoutsclone.hangouts.requests.UserLoginRequest;
import hangoutsclone.hangouts.response.AuthResponse;
import hangoutsclone.hangouts.services.CustomUserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private TokenProvider tokenProvider;
    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private CustomUserServiceImpl customUserServiceImpl;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();

        User isAlreadyRegisteredUser = userRepo.findByEmail(email);

        if (isAlreadyRegisteredUser != null) {
            throw new UserException("User already exists");
        }

        User newUser = new User();
        newUser.setEmail(email);
        newUser.setName(name);
        newUser.setPassword(passwordEncoder.encode(password));

        userRepo.save(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody UserLoginRequest user) throws UserException {
        String email = user.getEmail();
        String password = user.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse(jwt, true);

        // User existingUser = userRepo.findByEmail(email);

        // if (existingUser == null) {
        //     throw new UserException("User not found");
        // }
        // if (!passwordEncoder.matches(password, existingUser.getPassword())) {
        //     throw new UserException("Invalid login credentials");
        // }
        // return new ResponseEntity<>(authResponse, HttpStatus.OK);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    public Authentication authenticate(String email, String password) {
        UserDetails userDetails = customUserServiceImpl.loadUserByUsername(email);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public AuthController(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.customUserServiceImpl = customUserServiceImpl;
    }
}
