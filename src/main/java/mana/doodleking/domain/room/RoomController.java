package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.domain.user.dto.CreateUserRes;
import mana.doodleking.global.MessageSender;
import mana.doodleking.global.response.APIResponse;
import mana.doodleking.websocket.RequestChatContentsDto;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
}
