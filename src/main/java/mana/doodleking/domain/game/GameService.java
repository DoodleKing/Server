package mana.doodleking.domain.game;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.room.RoomService;
import mana.doodleking.domain.room.UserRoomService;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.RoomIdDTO;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.repository.RoomRepository;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GameService {
    private final RoomService roomService;
    private final UserRoomService userRoomService;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final UserRoomRepository userRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX_GAME = "GAME";
    private static final String PREFIX_TURN = "TURN";

    @Transactional
    public void startGame(Long userId, RoomIdDTO roomIdDTO) {
        User user = userRepository.findByIdOrThrow(userId);
        Room startRoom = roomRepository.findByIdOrThrow(roomIdDTO.getRoomId());

        canStartGame(startRoom, user);
        startRoom.setRoomState(RoomState.PLAY);

        setRedis(startRoom);
    }

    private void canStartGame(Room startRoom, User user) {
        roomService.checkRoomStatus(startRoom);
        userRoomService.checkUserStatus(startRoom, user);
    }

    private void setRedis(Room startRoom) {
        redisTemplate.opsForValue()
                .set(PREFIX_GAME + startRoom.getId(),
                        RedisGameDTO.from(startRoom, createScore(startRoom)),
                        Duration.ofSeconds((startRoom.getTime() * startRoom.getRound()) + 10));
    }

    private List<Map<String, Long>> createScore(Room room) {
        return userRoomRepository.findAllByRoom(room).stream()
                .map(UserRoom::getUser)
                .map(user -> {
                    Map<String, Long> scoreMap = new HashMap<>();
                    scoreMap.put(user.getName(), 0L);
                    return scoreMap;
                })
                .toList();
    }
}
