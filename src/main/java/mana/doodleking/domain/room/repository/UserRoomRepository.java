package mana.doodleking.domain.room.repository;

import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import mana.doodleking.domain.user.enums.UserState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    boolean existsUserRoomByUser(User user);
    UserRoom findUserRoomByUser(User user);
    boolean existsByRoomAndRole(Room room, UserRole userRole);
    List<UserRoom> findAllByRoom(Room room);
    List<UserRoom> findAllByRoomAndState(Room room, UserState userState);
}
