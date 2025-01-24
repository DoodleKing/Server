package mana.doodleking.domain.room.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.enums.Subject;

@Getter
@Builder
@AllArgsConstructor
public class UpdateRoomReq {
    private Long id;
    private String name;
    private Subject subject;
    private Long maxPlayer;
    private String password;
    private Long time;
    private Long round;
    private Boolean hint;
}
