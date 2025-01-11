package mana.doodleking.domain.room.dto;

import lombok.*;
import mana.doodleking.domain.room.Room;
import mana.doodleking.domain.room.RoomState;
import mana.doodleking.domain.room.Subject;

@Builder
@Getter
@AllArgsConstructor
public class RoomDetail {
    private Long id;
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long player;
    private Long time;
    private Long round;
    private Boolean hint;

    public static RoomDetail from(Room room) {
        return RoomDetail.builder()
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
