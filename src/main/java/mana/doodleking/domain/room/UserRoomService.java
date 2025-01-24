package mana.doodleking.domain.room;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;

    public void checkUserInRoom(User user) {
        if (userRoomRepository.existsUserRoomByUser(user))
            throw new RuntimeException("이미 Game Room 내부에 속해있습니다: " + user.getId());
    }

    public void isUserInRoom(User user, Room room) {
        if (!userRoomRepository.findUserRoomByUser(user).getRoom().equals(room))
            throw new RuntimeException("유저가 해당 방에 속해있지 않습니다. User: " + user.getId() + " Room: " + room.getId());
    }

    public void updateUserToLeader(Room room) {
        User user = userRoomRepository.findAllByRoom(room)
                .stream()
                .map(UserRoom::getUser)
                .min(Comparator.comparing(User::getId))
                .orElseThrow(() -> new RuntimeException("Room에 유저가 없습니다."));

        UserRoom userRoom = userRoomRepository.findUserRoomByUser(user);
        userRoom.setRole(UserRole.LEADER);

        userRoomRepository.save(userRoom);
    }
}
