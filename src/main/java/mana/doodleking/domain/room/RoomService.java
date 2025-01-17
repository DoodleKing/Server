package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.domain.user.User;
import mana.doodleking.domain.user.UserRole;
import mana.doodleking.domain.user.repository.UserRepository;
import mana.doodleking.global.response.APIResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;

    public RoomDetail createRoom(Long userId, PostRoomReq postRoomReq) {
        User user = userRepository.findUserById(userId);
        Room createdRoom = roomRepository.save(Room.from(postRoomReq));

        UserRoom userRoom = UserRoom.of(UserRole.LEADER, user, createdRoom);
        userRoomRepository.save(userRoom);

        return RoomDetail.from(createdRoom);
    }

    public List<RoomSimple> getRoomList() {
        List<Room> roomList = roomRepository.findAll();
        return roomList.stream()
                .map(RoomSimple::from)
                .toList();
    }
}
