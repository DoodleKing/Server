package mana.doodleking.global.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mana.doodleking.domain.room.dto.CreateRoomReq;
import mana.doodleking.domain.room.dto.RoomSimple;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Tag(name = "게임방 API", description = "게임방 생성, 조회 및 관련 기능 제공")
public interface RoomControllerDocs {
    @Operation(
        summary = "게임방 목록 조회(테스트용)",
        description = "현재 생성된 게임방 목록을 전체 조회"
    )
    List<RoomSimple> getRoomList();

    @GetMapping("/app/createRoom")
    @Operation(
        summary = "새로운 게임방 생성",
        description = """
        사용자가 새로운 게임방 생성\n
        생성된 방 정보는 요청한 사용자(/queue/user/{userId})에게 전송되며,
        전체 로비에 있는 사용자들(/topic/lobby)에게도 전체 게임방 목록이 전송됩니다.
        """
    )
    void createRoom(Long userId, CreateRoomReq postRoomReq);
}