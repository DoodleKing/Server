package mana.doodleking.domain.room.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import mana.doodleking.domain.user.enums.UserState;

@Builder
@Getter
public class UserStateDTO {
    Long userId;
    UserState state;

    public static UserStateDTO of(Long userId, UserState state) {
        return UserStateDTO.builder()
                .userId(userId)
                .state(state)
                .build();
    }
}
