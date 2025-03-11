package mana.doodleking.domain.game.dto;

import lombok.AllArgsConstructor;

import java.util.*;

@AllArgsConstructor
public class RedisTurnDTO {
    private Long drawingUser;
    private String theme;
    private List<Map<String, Boolean>> correct;
}
