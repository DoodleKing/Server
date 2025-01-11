package mana.doodleking.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.Room;
import mana.doodleking.domain.room.RoomState;
import mana.doodleking.domain.room.Subject;

@Builder
@Getter
@AllArgsConstructor
public class RoomSimple {
    private Long id;
    private String name;
    private RoomState roomState;
    private Long player;

    public static RoomSimple from(Room room) {
        return RoomSimple.builder()
                .id(room.getId())
                .name(room.getName())
                .roomState(room.getRoomState())
                .player(room.getPlayer())
                .build();
    }
}
