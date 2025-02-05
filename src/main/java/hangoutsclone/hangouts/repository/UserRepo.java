package hangoutsclone.hangouts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hangoutsclone.hangouts.entities.User;


public interface UserRepo extends JpaRepository<User, Integer> {
    
    public User findByEmail(String email);
    
    @Query("select u from User u where u.name like %:query% or u.email like %:query%")   
    public List<User> searchUser(@Param("query") String query);
}
