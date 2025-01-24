package mana.doodleking.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.user.domain.User;

@Getter
public class CreateUserRes {
    private Long userId;
    private String userName;
    private Long characterId;

    @Builder
    private CreateUserRes(Long userId, String userName, Long characterId) {
        this.userId = userId;
        this.userName = userName;
        this.characterId = characterId;
    }

    public static CreateUserRes from(User user) {
        return CreateUserRes.builder()
                .userId(user.getId())
                .userName(user.getName())
                .characterId(user.getCharacter().getId())
                .build();
    }
}
