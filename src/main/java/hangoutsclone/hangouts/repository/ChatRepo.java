package hangoutsclone.hangouts.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hangoutsclone.hangouts.entities.Chat;
import hangoutsclone.hangouts.entities.User;

public interface ChatRepo extends JpaRepository<Chat, Integer> {

    @Query("select c from Chat c join c.users u where u.id=:userId")
    public List<Chat> findByUserId(@Param("userId") Integer userId);
    
    @Query("select c from Chat c where c.isGroup=false and :user1 member of c.users and :user2 member of c.users")
    public Chat findChatByUser1AndUser2(@Param("user1") User user1, @Param("user2") User user2);
}
