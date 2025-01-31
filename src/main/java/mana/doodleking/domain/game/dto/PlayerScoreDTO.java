package mana.doodleking.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.user.domain.User;

@Getter
public class PlayerScoreDTO {
    private Long userNumber;
    private String userName;
    private Long score;

    @Builder
    private PlayerScoreDTO(Long userNumber, String userName) {
        this.userNumber = userNumber;
        this.userName = userName;
        this.score = 0L;
    }

    public static PlayerScoreDTO from(User user) {
        return PlayerScoreDTO.builder()
                .userNumber(user.getId())
                .userName(user.getName())
                .build();
    }
}
