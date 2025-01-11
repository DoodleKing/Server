package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.dto.RoomDetail;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.RoomSimple;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/api/room")
@RestController
public class RoomController {
    private final RoomService roomService;

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
}
