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
import mana.doodleking.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final UserService userService;
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
        User user = userService.getUserOrThrow(userId);
        Room enterRoom = getRoomOrThrow(enterRoomReq.getRoomId());

        // 해당 유저 방 소속 여부 확인
        checkUserInRoom(user);

        // 방 현재 인원 변경
        enterRoom.setCurPlayer(enterRoom.getCurPlayer() + 1L);

        // 유저 방 소속 관계 생성
        UserRoom userRoom = UserRoom.of(UserRole.MEMBER, user, enterRoom);
        userRoomRepository.save(userRoom);

        return RoomDetail.from(enterRoom);
    }

    private Room getRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 방 id: " + roomId));
    }

    private void checkUserInRoom(User user) {
        if (userRoomRepository.existsUserRoomByUser(user))
            throw new RuntimeException("이미 Game Room 내부에 속해있습니다: " + user.getId());
    }
}
