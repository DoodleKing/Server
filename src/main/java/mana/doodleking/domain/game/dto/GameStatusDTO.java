package mana.doodleking.domain.game.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GameStatusDTO {
    private List<PlayerScoreDTO> gameStatus;
}
