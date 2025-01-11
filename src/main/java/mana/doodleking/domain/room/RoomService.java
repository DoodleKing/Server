package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.dto.PostRoomReq;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(PostRoomReq postRoomReq) {
        Room room = Room.from(postRoomReq);
        return roomRepository.save(room);
    }

    public List<Room> getRoomList() {
        return roomRepository.findAll();
    }
}
