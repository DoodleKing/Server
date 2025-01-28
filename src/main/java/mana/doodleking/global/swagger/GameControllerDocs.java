package mana.doodleking.global.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mana.doodleking.domain.room.dto.CreateRoomReq;
import mana.doodleking.domain.room.dto.RoomIdDTO;
import mana.doodleking.domain.room.dto.RoomSimple;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "게임 API", description = "게임 플레이와 관련된 기능 제공")
public interface GameControllerDocs {

    @GetMapping("/startGame")
    @Operation(
        summary = "게임 시작",
        description = """
        사용자가 게임을 시작합니다.\n
        전체 게임방 목록이 요청한 사용자(/topic/lobby)에게 전송되며,
        방의 참여자들(/topic/room)에게 GAME START라는 메시지가 전송됩니다.
        """
    )
    void startGame(@Header("userId") Long userId, @Valid RoomIdDTO roomIdDTO);
}