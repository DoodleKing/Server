package mana.doodleking.domain.room.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

@Getter
public class CreateRoomReq {
    @NotBlank(message = "방 이름은 필수입니다.")
    @Size(max = 50, message = "방 이름은 최대 50자까지 가능합니다.")
    private String name;

    @NotNull(message = "주제 설정은 필수입니다.")
    private Subject subject;

    private RoomState roomState;

    @NotNull(message = "최대 플레이어 수 설정은 필수입니다.")
    @Min(value = 2, message = "최대 플레이어 수는 최소 2명 이상이어야 합니다.")
    @Max(value = 8, message = "최대 플레이어 수는 최대 8명까지 가능합니다.")
    private Long maxPlayer;

    @Size(max = 20, message = "비밀번호는 최대 20자까지 가능합니다.")
    private String password;

    @NotNull(message = "라운드 시간 설정은 필수입니다.")
    @Min(value = 60, message = "라운드 시간은 최소 60초 이상이어야 합니다.")
    @Max(value = 80, message = "라운드 시간은 최대 80초까지 가능합니다.")
    private Long time;

    @NotNull(message = "게임 라운드 설정은 필수입니다.")
    @Min(value = 1, message = "게임 라운드는 최소 1라운드 이상이어야 합니다.")
    @Max(value = 5, message = "게임 라운드는 최대 5라운드까지 가능합니다.")
    private Long round;

    @NotNull(message = "힌트 여부 설정은 필수입니다.")
    private Boolean hint;
}

