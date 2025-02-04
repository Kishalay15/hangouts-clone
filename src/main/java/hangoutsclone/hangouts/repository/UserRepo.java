package hangoutsclone.hangouts.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hangoutsclone.hangouts.entities.User;


public interface UserRepo extends JpaRepository<User, Integer> {
    
    public User findByEmail(String email);
}
