package mana.doodleking.domain.room.dto;

import lombok.Getter;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

@Getter
public class PostRoomReq {
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long maxPlayer;
    private String password;
    private Long time;
    private Long round;
    private Boolean hint;
}

