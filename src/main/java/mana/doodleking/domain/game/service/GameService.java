package mana.doodleking.domain.game.service;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.game.dto.GameStatusDTO;
import mana.doodleking.domain.game.dto.PlayerScoreDTO;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.domain.UserRoom;
import mana.doodleking.domain.room.dto.RoomIdDTO;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.repository.RoomRepository;
import mana.doodleking.domain.room.repository.UserRoomRepository;
import mana.doodleking.domain.room.service.RoomService;
import mana.doodleking.domain.room.service.UserRoomService;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserState;
import mana.doodleking.domain.user.repository.UserRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    public GameStatusDTO startGame(Long userId, RoomIdDTO roomIdDTO) {
        User user = userRepository.findByIdOrThrow(userId);
        Room startRoom = roomRepository.findByIdOrThrow(roomIdDTO.getRoomId());

        canStartGame(startRoom, user);
        startRoom.setRoomState(RoomState.PLAY);

        setRedis(startRoom);

        return new GameStatusDTO(userRoomRepository.findAllByRoom(startRoom).stream()
                .map(UserRoom::getUser)
                .map(PlayerScoreDTO::from)
                .toList());
    }

    private void canStartGame(Room startRoom, User user) {
        roomService.checkRoomStatus(startRoom);
        userRoomService.checkUserStatus(startRoom, user);
    }

    private void setRedis(Room startRoom) {
        final String HASH_KEY = "GAME" + startRoom.getId();

        // redis에 Key-Value 형태로 값 저장
        userRoomRepository.findAllByRoom(startRoom).stream()
                .map(UserRoom::getUser)
                .forEach(user -> redisTemplate.opsForHash().put(HASH_KEY, user.getName(), "0"));

        // TTL 설정
        redisTemplate.expire(HASH_KEY, startRoom.getTime() * (startRoom.getRound() + 1), TimeUnit.SECONDS);
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

    @Transactional
    public GameStatusDTO endGame(Long roomId) {
        Room room = roomRepository.findByIdOrThrow(roomId);

        // 모든 사용자 not ready 상태로 변경
        userRoomRepository.findAllByRoom(room).stream()
                .map(UserRoom::getUser)
                .forEach(user -> userRoomService.setUserState(user.getId(), UserState.NOT_READY));

        // 게임방 정보 갱신
        room.setRoomState(RoomState.WAIT);

        // 완료된 게임 정보 반환
        return getGameResult(roomId);
    }

    private GameStatusDTO getGameResult(Long roomId) {
        String key = PREFIX_GAME + roomId;
        return (GameStatusDTO) redisTemplate.opsForValue().get(key);
    }
}
