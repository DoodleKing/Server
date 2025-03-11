package mana.doodleking.domain.room.service;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.*;
import mana.doodleking.domain.room.enums.RoomState;
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
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRoomService userRoomService;

    @Transactional
    public RoomDetail createRoom(Long userId, CreateRoomReq postRoomReq) {
        Room createdRoom = roomRepository.save(Room.from(postRoomReq));
        User user = userRepository.findByIdOrThrow(userId);

        userRoomRepository.save(UserRoom.of(UserRole.LEADER, user, createdRoom));

        return RoomDetail.from(createdRoom);
    }

    public RoomDetail updateRoom(Long userId, UpdateRoomReq updateRoomReq) {
        Room room = roomRepository.findByIdOrThrow(updateRoomReq.getId());
        User user = userRepository.findByIdOrThrow(userId);

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
        Room enterRoom = roomRepository.findByIdOrThrow(enterRoomReq.getRoomId());
        User user = userRepository.findByIdOrThrow(userId);

        // 해당 유저 방 소속 여부 확인
        userRoomService.checkUserInRoom(user);

        // 방 현재 인원 변경
        canEnterRoom(enterRoom);

        // 유저 방 소속 관계 생성
        userRoomRepository.save(UserRoom.of(UserRole.MEMBER, user, enterRoom));
        return RoomDetail.from(enterRoom);
    }

    @Transactional
    public RoomDetail quitRoom(Long userId, RoomIdDTO roomIdDTO) {
        Room quitRoom = roomRepository.findByIdOrThrow(roomIdDTO.getRoomId());
        User user = userRepository.findByIdOrThrow(userId);

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

    private void deleteRoom(Long roomId) {
        roomRepository.delete(roomRepository.findByIdOrThrow(roomId));
    }

    private void canEnterRoom(Room enterRoom) {
        if (!enterRoom.getRoomState().equals(RoomState.WAIT))
            throw new RuntimeException("방에 입장이 불가능합니다. : " + enterRoom.getRoomState());

        if (enterRoom.getCurPlayer().equals(enterRoom.getMaxPlayer()))
            throw new RuntimeException("해당 방의 인원이 모두 찼습니다. : " + enterRoom.getId());

        enterRoom.setCurPlayer(enterRoom.getCurPlayer() + 1L);
        roomRepository.save(enterRoom);
    }

    public void checkRoomStatus(Room startRoom) {
        // 방 인원이 2명 이상인지
        if (startRoom.getCurPlayer() < 2)
            throw new RuntimeException("시작 인원이 부족합니다. : " + startRoom.getCurPlayer());

        // 방 상태가 WAIT인지
        if (!startRoom.getRoomState().equals(RoomState.WAIT))
            throw new RuntimeException("게임을 시작할 수 없는 상태입니다. : " + startRoom.getRoomState());
    }
}
