package mana.doodleking.domain.game.controller;

import jakarta.persistence.OptimisticLockException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mana.doodleking.domain.game.dto.GameStatusDTO;
import mana.doodleking.domain.game.service.GameService;
import mana.doodleking.domain.room.dto.RoomIdDTO;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.domain.room.service.RoomService;
import mana.doodleking.global.MessageSender;
import mana.doodleking.global.swagger.GameControllerDocs;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Controller
public class GameController implements GameControllerDocs {
    private final GameService gameService;
    private final RoomService roomService;
    private final MessageSender messageSender;

    @MessageMapping("/startGame")
    public void startGame(@Header("userId") Long userId, @Valid RoomIdDTO roomIdDTO) {
        try {
            GameStatusDTO gameStatus = gameService.startGame(userId, roomIdDTO);
            messageSender.send("/topic/room/" + roomIdDTO.getRoomId(), gameStatus);

            List<RoomSimple> roomList = roomService.getRoomList();
            messageSender.send("/topic/lobby", roomList);
        }
        catch (OptimisticLockException e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, "동시성 문제 발생: 다시 시도해주세요.");
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }

    @MessageMapping("/endGame")
    public void endGame(@Header("userId") Long userId, @Valid RoomIdDTO roomIdDTO) {
        try {
            GameStatusDTO gameResult = gameService.endGame(roomIdDTO.getRoomId());
            log.info(gameResult.toString());
            messageSender.send("/topic/room/" + roomIdDTO.getRoomId(), gameResult);

            List<RoomSimple> roomList = roomService.getRoomList();
            messageSender.send("/topic/lobby", roomList);
        }
        catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }
}
