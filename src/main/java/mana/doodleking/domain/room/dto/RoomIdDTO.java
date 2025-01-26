package mana.doodleking.domain.room.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class RoomIdDTO {
    @Positive
    private Long roomId;
}
