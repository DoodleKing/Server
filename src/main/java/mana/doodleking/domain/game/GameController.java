package mana.doodleking.domain.game;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;

@RequiredArgsConstructor
public class GameController {
    private GameService gameService;

    @MessageMapping("/startGame")
    public void startGame(@Header("userId") Long userId, @Valid StartGameInfo startGameInfo) {
        gameService.startGame(userId, startGameInfo);
        // 초 카운팅?
    }
}
