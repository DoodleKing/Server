package mana.doodleking.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mana.doodleking.domain.room.RoomState;
import mana.doodleking.domain.room.Subject;

@Getter
public class PostRoomReq {
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long player;
    private String password;
    private Long time;
    private Long round;
    private Boolean hint;
}
