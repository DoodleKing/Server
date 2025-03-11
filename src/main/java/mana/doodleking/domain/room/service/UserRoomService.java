package mana.doodleking.domain.room.service;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.UserStateDTO;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import mana.doodleking.domain.user.enums.UserState;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;

    public void checkUserInRoom(User user) {
        if (userRoomRepository.existsUserRoomByUser(user))
            throw new RuntimeException("이미 Game Room 내부에 속해있습니다: " + user.getId());
    }

    public void isUserInRoom(User user, Room room) {
        if (!userRoomRepository.findUserRoomByUser(user).getRoom().equals(room))
            throw new RuntimeException("유저가 해당 방에 속해있지 않습니다. User: " + user.getId() + " Room: " + room.getId());
    }

    public Long getRoomIdByUserId(Long userId) {
        User user = userRepository.findByIdOrThrow(userId);
        Room room = userRoomRepository.findUserRoomByUser(user).getRoom();
        return room.getId();
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

    public UserStateDTO setUserState(Long userId, UserState state) {
        User user = userRepository.findByIdOrThrow(userId);

        UserRoom userRoom = userRoomRepository.findUserRoomByUser(user);
        userRoom.setState(state);

        userRoomRepository.save(userRoom);
        return UserStateDTO.of(userId, state);
    }

    public void checkUserStatus(Room room, User user) {
        // 사용자 All Ready
        if (!userRoomRepository.findAllByRoomAndState(room, UserState.NOT_READY).isEmpty())
            throw new RuntimeException("준비되지 않은 유저가 있습니다.");

        // 요청한 유저가 방장인지
        if (userRoomRepository.findUserRoomByUser(user).getRole().equals(UserRole.MEMBER))
            throw new RuntimeException("방장이 아닙니다.");
    }
}
