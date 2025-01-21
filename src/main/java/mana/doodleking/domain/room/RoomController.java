package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomSimple;
import mana.doodleking.domain.user.dto.CreateUserRes;
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
    private final SimpMessagingTemplate messagingTemplate;
    @PostMapping
    public RoomDetail createRoom(@RequestBody PostRoomReq postRoomReq) {
        return roomService.createRoom(postRoomReq);
    }

    @GetMapping
    public List<RoomSimple> getRoomList() {
        return roomService.getRoomList();
    }

    @MessageMapping("/createRoom")
    @SendTo("/topic/lobby")
    public APIResponse<List<RoomSimple>> sendMessage(@Header("simpSessionId") String userId, PostRoomReq postRoomReq) {
        String dest = "/queue/user/" + userId;
        RoomDetail createdRoom = roomService.createRoom(postRoomReq);
        log.info(dest);
        messagingTemplate.convertAndSend(dest, APIResponse.success(createdRoom));

        List<RoomSimple> roomList = roomService.getRoomList();
        return APIResponse.success(roomList);
    }
}
