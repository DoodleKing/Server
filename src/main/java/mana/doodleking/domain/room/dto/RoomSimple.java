package mana.doodleking.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.Room;
import mana.doodleking.domain.room.RoomState;

@Builder
@Getter
@AllArgsConstructor
public class RoomSimple {
    private Long id;
    private String name;
    private RoomState roomState;
    private Long maxPlayer;
    private Long curPlayer;
    private boolean privateRoom;

    public static RoomSimple from(Room room) {
        return RoomSimple.builder()
                .id(room.getId())
                .name(room.getName())
                .roomState(room.getRoomState())
                .maxPlayer(room.getMaxPlayer())
                .curPlayer(room.getCurPlayer())
                .privateRoom(room.getPassword() != null)
                .build();
    }
}
