package mana.doodleking.domain.room.dto;

import lombok.*;
import mana.doodleking.domain.room.domain.Room;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

@Builder
@Getter
@AllArgsConstructor
public class RoomDetail {
    private Long id;
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long maxPlayer;
    private Long curPlayer;
    private Long time;
    private Long round;
    private Boolean hint;

    public static RoomDetail from(Room room) {
        return RoomDetail.builder()
                .id(room.getId())
                .name(room.getName())
                .subject(room.getSubject())
                .roomState(room.getRoomState())
                .maxPlayer(room.getMaxPlayer())
                .curPlayer(room.getCurPlayer())
                .time(room.getTime())
                .round(room.getRound())
                .hint(room.getHint())
                .build();
    }
}
