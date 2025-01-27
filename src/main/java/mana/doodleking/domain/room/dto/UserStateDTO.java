package mana.doodleking.domain.room.dto;

import lombok.Builder;
import lombok.Data;
import mana.doodleking.domain.user.enums.UserState;

@Builder
@Data
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
