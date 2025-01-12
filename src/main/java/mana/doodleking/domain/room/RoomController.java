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
        Room createdRoom = roomService.createRoom(postRoomReq);
        return RoomDetail.from(createdRoom);
    }

    @GetMapping
    public List<RoomSimple> getRoomList() {
        List<Room> roomList = roomService.getRoomList();
        return roomList.stream()
                .map(RoomSimple::from)
                .toList();
    }
    @MessageMapping("/init")
    @SendTo("/topic/init")
    public RequestChatContentsDto init(@Header("simpSessionId") String sessionId, RequestChatContentsDto message) {
        message.setContents(sessionId);
        return message;
    }

    @MessageMapping("/test")
    @SendTo("/topic/lobby")
    public APIResponse<List<RoomSimple>> sendMessage(@Header("simpSessionId") String userId, RequestChatContentsDto message) {
        List<Room> roomList = roomService.getRoomList();
        String dest = "/topic/my/" + userId;
        log.info(dest);
        messagingTemplate.convertAndSend(dest, APIResponse.success(roomList.stream().map(RoomSimple::from).toList()));
        return APIResponse.success(roomList.stream().map(RoomSimple::from).toList());
    }
}
