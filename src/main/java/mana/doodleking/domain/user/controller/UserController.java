package mana.doodleking.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.user.dto.CreateUserReq;
import mana.doodleking.domain.user.dto.CreateUserRes;
import mana.doodleking.domain.user.service.UserService;
import mana.doodleking.global.response.APIResponse;
import mana.doodleking.global.swagger.UserControllerDocs;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserControllerDocs {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<APIResponse<CreateUserRes>> userCreate(@RequestBody @Valid CreateUserReq createUserReq) throws Exception {
        // 유저명 중복 검증
        userService.validUserName(createUserReq.getUserName());
        // 캐릭터 ID 존재 검증
        userService.validCharacter(createUserReq.getCharacterId());

        return ResponseEntity.ok(APIResponse.success(CreateUserRes.from(userService.createUser(createUserReq))));
    }
}
