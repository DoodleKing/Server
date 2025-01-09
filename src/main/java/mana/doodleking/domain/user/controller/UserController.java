package mana.doodleking.domain.user.controller;

import lombok.RequiredArgsConstructor;
import mana.doodleking.domain.user.dto.CreateUserReq;
import mana.doodleking.domain.user.dto.CreateUserRes;
import mana.doodleking.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("")
    private CreateUserRes userCreate(@RequestBody CreateUserReq createUserReq) throws Exception {
        // 유저명 중복 검증
        userService.validUserName(createUserReq.getUserName());
        // 캐릭터 ID 존재 검증
        userService.validCharacter(createUserReq.getCharacterId());

        return CreateUserRes.from(userService.createUser(createUserReq));
    }
}
