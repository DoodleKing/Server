package mana.doodleking.domain.game;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.room.RoomService;
import mana.doodleking.domain.room.UserRoomService;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.repository.RoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameService {
    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private static final String PREFIX_GAME = "GAME";
    private static final String PREFIX_TURN = "TURN";

    @Transactional
    public void startGame(Long userId, StartGameInfo startGameInfo) {
        User user = userRepository.findByIdOrThrow(userId);
        Room startRoom = roomRepository.findByIdOrThrow(startGameInfo.getId());

        canStartGame(startRoom, user);

    }

    private void canStartGame(Room startRoom, User user) {
        roomService.checkRoomStatus(startRoom);
        userRoomService.checkUserStatus(startRoom, user);
    }
}
