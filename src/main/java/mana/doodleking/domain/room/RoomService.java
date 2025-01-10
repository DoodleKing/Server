package mana.doodleking.domain.room;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.dto.CreateRoomRes;
import mana.doodleking.domain.room.dto.PostRoomReq;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    public Room createRoom(PostRoomReq postRoomReq) {
        Room room = Room.from(postRoomReq);
        return roomRepository.save(room);
    }
}
