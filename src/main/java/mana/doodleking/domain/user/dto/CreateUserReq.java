package mana.doodleking.domain.user.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
public class CreateUserReq {
    @NotNull(message = "값을 입력해주세요.")
    @Positive(message = "양수 형태여야 합니다.")
    private Long characterId;

    @NotBlank(message = "값을 입력해주세요.")
    @Pattern(regexp = "^[가-힣a-zA-Z]*$", message = "한글 또는 영어로 입력해주세요.")
    @Size(min = 3, max = 10, message = "3-10자 이내로 입력해주세요.")
    private String userName;
}
