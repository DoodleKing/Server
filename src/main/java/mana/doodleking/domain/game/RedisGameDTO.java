package mana.doodleking.domain.game;

import lombok.Builder;
import lombok.Getter;
import mana.doodleking.domain.room.domain.Room;

import java.util.*;

@Getter
public class RedisGameDTO {
    private Long totalTurn;
    private Long curTurn;
    private Long player;
    private List<Map<String, Long>> score;

    @Builder
    private RedisGameDTO(Long totalTurn, Long curTurn, Long player, List<Map<String, Long>> score) {
        this.totalTurn = totalTurn;
        this.curTurn = curTurn;
        this.player = player;
        this.score = score;
    }

    public static RedisGameDTO from(Room room, List<Map<String, Long>> score) {
        return RedisGameDTO.builder()
                .totalTurn(room.getRound() * room.getCurPlayer())
                .curTurn(0L)
                .player(room.getCurPlayer())
                .score(score)
                .build();
    }
}
