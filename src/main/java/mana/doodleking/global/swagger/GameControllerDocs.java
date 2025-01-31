package mana.doodleking.global.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mana.doodleking.domain.room.dto.RoomIdDTO;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "게임 API", description = "게임 플레이와 관련된 기능 제공")
public interface GameControllerDocs {

    @GetMapping("/app/startGame")
    @Operation(
        summary = "게임 시작",
        description = """
        사용자가 게임을 시작합니다.\n
        전체 게임방 목록이 요청한 사용자(/topic/lobby)에게 전송되며,
        방의 참여자들(/topic/room)에게 GAME START라는 메시지가 전송됩니다.
        """
    )
    void startGame(@Header("userId") Long userId, @Valid RoomIdDTO roomIdDTO);

    @GetMapping("/app/endGame")
    @Operation(
            summary = "게임 종료",
            description = """
        서버 내부에서 호출되는 게임 종료입니다. (마지막 라운드에 모든 사용자가 정답을 맞추거나, 라운드 시간이 종료되면 호출)\n
        전체 게임방 목록이 요청한 사용자(/topic/lobby)에게 전송되며,
        게임 결과가 참여자들에게 전송되며, 방에 대한 정보(사용자 준비 상태, 방의 상태)가 갱신됩니다.
        """
    )
    void endGame(@Header("userId") Long userId, @Valid RoomIdDTO roomIdDTO);
}