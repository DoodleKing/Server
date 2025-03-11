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
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        final String key = PREFIX_GAME + startRoom.getId();
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();

        hashOps.put(key, "totalTurn", startRoom.getRound());
        hashOps.put(key, "curTurn", 0);
        hashOps.put(key, "player", startRoom.getCurPlayer());
        hashOps.put(key, "score", createScore(startRoom));

        // TTL 설정
        redisTemplate.expire(key, startRoom.getTime() * (startRoom.getRound() + 1), TimeUnit.SECONDS);
    }

    private List<Map<String, Long>> createScore(Room room) {
        return userRoomRepository.findAllByRoom(room).stream()
                .map(UserRoom::getUser)
                .map(user -> {
                    Map<String, Long> scoreMap = new HashMap<>();
                    scoreMap.put(String.valueOf(user.getId()), 0L);
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

    public GameStatusDTO getGameResult(Long roomId) {
        String key = PREFIX_GAME + roomId;
        HashOperations<String, String, Object> hashOps = redisTemplate.opsForHash();
        Object scoreObj = hashOps.get(key, "score");
        List<?> scoreList = (List<?>) scoreObj;

        // Redis에 저장한 score 리스트를 PlayerScoreDTO애 매핑
        List<PlayerScoreDTO> playerScoreDTOList = scoreList.stream()
                .filter(item -> item instanceof Map<?, ?>)
                .map(this::mapToPlayerScoreDTO)
                .collect(Collectors.toList());

        return new GameStatusDTO(playerScoreDTOList);
    }

    private PlayerScoreDTO mapToPlayerScoreDTO(Object item) {
        Map<String, Integer> scoreMap = (Map<String, Integer>) item;
        Long userNumber = Long.valueOf(scoreMap.keySet().iterator().next());
        Long score = scoreMap.get(userNumber.toString()).longValue();

        // 사용자 id 따른 User 객체 조회
        User user = userRepository.findByIdOrThrow(userNumber);

        // PlayerScoreDTO 생성
        return PlayerScoreDTO.from(user, score);
    }
}
