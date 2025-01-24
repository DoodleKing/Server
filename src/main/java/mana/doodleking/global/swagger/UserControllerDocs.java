package mana.doodleking.global.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import mana.doodleking.domain.user.dto.CreateUserReq;
import mana.doodleking.domain.user.dto.CreateUserRes;
import mana.doodleking.global.response.APIResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "사용자 API")
public interface UserControllerDocs {

    @Operation(summary = "사용자 생성", description = "새로운 사용자를 생성합니다.")
    ResponseEntity<APIResponse<CreateUserRes>> userCreate(CreateUserReq createUserReq) throws Exception;
}
