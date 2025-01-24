package mana.doodleking.domain.room.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.enums.Subject;

@Getter
@Builder
@AllArgsConstructor
public class UpdateRoomReq {
    @NotNull(message = "Room ID는 필수입니다.")
    private Long id;

    @NotBlank(message = "방 이름은 필수입니다.")
    @Size(max = 50, message = "방 이름은 최대 50자까지 가능합니다.")
    private String name;

    private Subject subject;

    @Min(value = 2, message = "최대 플레이어 수는 최소 2명 이상이어야 합니다.")
    @Max(value = 8, message = "최대 플레이어 수는 최대 8명까지 가능합니다.")
    private Long maxPlayer;

    @Size(max = 20, message = "비밀번호는 최대 20자까지 가능합니다.")
    private String password;

    @Min(value = 60, message = "라운드 시간은 최소 60초 이상이어야 합니다.")
    @Max(value = 80, message = "라운드 시간은 최대 80초까지 가능합니다.")
    private Long time;

    @Min(value = 1, message = "게임 라운드는 최소 1라운드 이상이어야 합니다.")
    @Max(value = 5, message = "게임 라운드는 최대 5라운드까지 가능합니다.")
    private Long round;
    private Boolean hint;
}
