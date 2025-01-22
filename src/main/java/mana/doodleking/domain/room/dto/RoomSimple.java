package mana.doodleking.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

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
    private Subject subject;

    public static RoomSimple from(Room room) {
        return RoomSimple.builder()
                .id(room.getId())
                .name(room.getName())
                .roomState(room.getRoomState())
                .maxPlayer(room.getMaxPlayer())
                .curPlayer(room.getCurPlayer())
                .privateRoom(room.getPassword() != null)
                .subject(room.getSubject())
                .build();
    }
}
