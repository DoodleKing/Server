package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.EnterRoomReq;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.domain.room.repository.RoomRepository;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;

    public RoomDetail createRoom(Long userId, PostRoomReq postRoomReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자 id: " + userId));
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

    @Transactional
    public RoomDetail enterRoom(Long userId, EnterRoomReq enterRoomReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자 id: " + userId));
        Room enterRoom = roomRepository.findById(enterRoomReq.getRoomId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 방 id: " + enterRoomReq.getRoomId()));

        // 방 현재 인원 변경
        enterRoom.setCurPlayer(enterRoom.getCurPlayer() + 1L);

        // 유저 방 소속 관계 생성
        UserRoom userRoom = UserRoom.of(UserRole.MEMBER, user, enterRoom);
        userRoomRepository.save(userRoom);

        return RoomDetail.from(enterRoom);
    }
}
