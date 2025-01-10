package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.dto.CreateRoomRes;
import mana.doodleking.domain.room.dto.PostRoomReq;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@AllArgsConstructor
@RequestMapping("/api/room")
@RestController
public class RoomController {
    private final RoomService roomService;

    @PostMapping
    public CreateRoomRes createRoom(@RequestBody PostRoomReq postRoomReq) {
        Room createdRoom = roomService.createRoom(postRoomReq);
        return CreateRoomRes.from(createdRoom);
    }
}
