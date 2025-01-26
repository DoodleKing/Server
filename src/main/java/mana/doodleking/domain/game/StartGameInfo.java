package mana.doodleking.domain.game;

import jakarta.validation.constraints.*;
import lombok.Getter;
import mana.doodleking.domain.room.enums.Subject;

@Getter
public class StartGameInfo {
    @Positive
    private Long id;
    @NotBlank(message = "방 이름은 필수입니다.")
    @Size(max = 50, message = "방 이름은 최대 50자까지 가능합니다.")
    private String name;
    @NotNull(message = "주제 설정은 필수입니다.")
    private Subject subject;
    @NotNull(message = "최대 플레이어 수 설정은 필수입니다.")
    @Min(value = 2, message = "최대 플레이어 수는 최소 2명 이상이어야 합니다.")
    @Max(value = 8, message = "최대 플레이어 수는 최대 8명까지 가능합니다.")
    private Long player;
    @Positive
    private Long time;
    @Positive
    private Long round;
    private Boolean hint;
}
