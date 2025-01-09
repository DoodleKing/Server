package mana.doodleking.domain.user.dto;

import lombok.Getter;

@Getter
public class CreateUserReq {
    private Long characterId;
    private String userName;
}
