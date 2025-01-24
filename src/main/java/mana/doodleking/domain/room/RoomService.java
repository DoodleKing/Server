package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.*;
import mana.doodleking.domain.room.repository.RoomRepository;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import mana.doodleking.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final UserService userService;
    private final UserRoomService userRoomService;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;

    @Transactional
    public RoomDetail createRoom(Long userId, PostRoomReq postRoomReq) {
        User user = userService.getUserOrThrow(userId);
        Room createdRoom = roomRepository.save(Room.from(postRoomReq));

        userRoomRepository.save(UserRoom.of(UserRole.LEADER, user, createdRoom));

        return RoomDetail.from(createdRoom);
    }

    public RoomDetail updateRoom(Long userId, UpdateRoomReq updateRoomReq) {
        User user = userService.getUserOrThrow(userId);
        Room room = roomRepository.findById(updateRoomReq.getId()).orElseThrow(() -> new RuntimeException("존재하지 않는 room id: " + updateRoomReq.getId()));

        userRoomService.isUserInRoom(user, room);

        Room updatedRoom = room.update(updateRoomReq);

        return RoomDetail.from(updatedRoom);
    }

    public List<RoomSimple> getRoomList() {
        List<Room> roomList = roomRepository.findAll();
        return roomList.stream()
                .map(RoomSimple::from)
                .toList();
    }

    @Transactional
    public RoomDetail enterRoom(Long userId, RoomIdDTO enterRoomReq) {
        User user = userService.getUserOrThrow(userId);
        Room enterRoom = getRoomOrThrow(enterRoomReq.getRoomId());

        // 해당 유저 방 소속 여부 확인
        userRoomService.checkUserInRoom(user);

        // 방 현재 인원 변경
        enterRoom.setCurPlayer(enterRoom.getCurPlayer() + 1L);

        // 유저 방 소속 관계 생성
        userRoomRepository.save(userRoomRepository.save(UserRoom.of(UserRole.MEMBER, user, enterRoom)));
        return RoomDetail.from(enterRoom);
    }

    @Transactional
    public RoomDetail quitRoom(Long userId, RoomIdDTO roomIdDTO) {
        User user = userService.getUserOrThrow(userId);
        Room quitRoom = getRoomOrThrow(roomIdDTO.getRoomId());

        // 해당 유저 방 소속 여부 확인
        userRoomService.isUserInRoom(user, quitRoom);

        // 방 현재 인원 변경 및 유저 방 소속 관계 삭제
        quitRoom.setCurPlayer(quitRoom.getCurPlayer() - 1L);
        userRoomRepository.delete(userRoomRepository.findUserRoomByUser(user));

        // 인원 0명인 경우 방 폭파 및 방장이 나가는 경우 권한 위임
        if (quitRoom.getCurPlayer().equals(0L))
            deleteRoom(roomIdDTO.getRoomId());
        else if (!userRoomRepository.existsByRoomAndRole(quitRoom, UserRole.LEADER))
            userRoomService.updateUserToLeader(quitRoom);

        return RoomDetail.from(quitRoom);
    }

    private Room getRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 방 id: " + roomId));
    }

    private void deleteRoom(Long roomId) {
        roomRepository.delete(getRoomOrThrow(roomId));
    }
}
