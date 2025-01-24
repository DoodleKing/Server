package mana.doodleking.domain.room;

import jakarta.persistence.OptimisticLockException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mana.doodleking.domain.room.dto.EnterRoomReq;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.global.MessageSender;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/room")
@RestController
@Slf4j
public class RoomController {
    private final RoomService roomService;
    private final MessageSender messageSender;
    @GetMapping
    public List<RoomSimple> getRoomList() {
        return roomService.getRoomList();
    }

    @MessageMapping("/createRoom")
    public void sendMessage(@Header("userId") Long userId, PostRoomReq postRoomReq) {
        try {
            RoomDetail createdRoom = roomService.createRoom(userId, postRoomReq);
            messageSender.send("/queue/user/" + userId, createdRoom);

            List<RoomSimple> roomList = getRoomList();
            messageSender.send("/topic/lobby", roomList);
        } catch (Exception e) {
            log.warn(e.getMessage());
            messageSender.sendError("/queue/user/" + userId, e.getMessage());
        }
    }

    @MessageMapping("/enterRoom")
    public void enterRoom(@Header("userId") Long userId, EnterRoomReq enterRoomReq) {
        try {
            RoomDetail enterRoom = roomService.enterRoom(userId, enterRoomReq);
            messageSender.send("/queue/user/" + userId, enterRoom);

            messageSender.send("/topic/room/" + enterRoomReq.getRoomId(), enterRoom);

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
}
