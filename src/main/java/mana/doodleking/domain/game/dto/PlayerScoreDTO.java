package mana.doodleking.domain.game.dto;

import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.user.domain.User;

@Getter
@Builder
public class PlayerScoreDTO {
    private Long userNumber;
    private String userName;
    private Long score;

    public static PlayerScoreDTO from(User user) {
        return PlayerScoreDTO.builder()
                .userNumber(user.getId())
                .userName(user.getName())
                .score(0L)
                .build();
    }

    public static PlayerScoreDTO from(User user, Long score) {
        return PlayerScoreDTO.builder()
                .userNumber(user.getId())
                .userName(user.getName())
                .score(score)
                .build();
    }
}
