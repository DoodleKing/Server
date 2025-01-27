package mana.doodleking.global.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import mana.doodleking.domain.room.dto.UserStateDTO;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "사용자-게임방 API", description = "게임 준비 상태를 관리하는 API")
public interface UserRoomControllerDocs {

    @Operation(
        summary = "사용자의 준비 상태를 READY로 변경",
        description = "변경된 사용자의 정보는 요청한 사용자가 속한 방(/topic/room/{roomId})에 전송",
        responses = {
            @ApiResponse(
                description = "변경된 사용자의 아이디와 준비 상태",
                content = @Content(schema = @Schema(implementation = UserStateDTO.class))
            )
        }
    )
    @GetMapping("/app/userReady")
    void setUserReady(@Header("userId") Long userId);

    @Operation(
        summary = "사용자의 준비 상태를 NOT_READY로 변경",
        description = "변경된 사용자의 정보는 요청한 사용자가 속한 방(/topic/room/{roomId})에 전송",
        responses = {
            @ApiResponse(
                description = "변경된 사용자의 아이디와 준비 상태",
                content = @Content(schema = @Schema(implementation = UserStateDTO.class))
            )
        }
    )
    @GetMapping("/app/userNotReady")
    void setUserNotReady(@Header("userId") Long userId);
}