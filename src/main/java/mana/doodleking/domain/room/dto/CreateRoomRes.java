package mana.doodleking.domain.room.dto;

import lombok.*;
import mana.doodleking.domain.room.Room;
import mana.doodleking.domain.room.RoomState;
import mana.doodleking.domain.room.Subject;

@Builder
@Getter
@AllArgsConstructor
public class CreateRoomRes {
    private Long id;
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long player;
    private Long time;
    private Long round;
    private Boolean hint;

    public static CreateRoomRes from(Room room) {
        return CreateRoomRes.builder()
                .id(room.getId())
                .name(room.getName())
                .subject(room.getSubject())
                .roomState(room.getRoomState())
                .player(room.getPlayer())
                .time(room.getTime())
                .round(room.getRound())
                .hint(room.getHint())
                .build();
    }
}
