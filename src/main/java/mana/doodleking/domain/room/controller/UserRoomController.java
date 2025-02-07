package mana.doodleking.domain.room.controller;

import lombok.AllArgsConstructor;
import mana.doodleking.domain.room.service.UserRoomService;
import mana.doodleking.domain.room.dto.UserStateDTO;
import mana.doodleking.domain.user.enums.UserState;
import mana.doodleking.global.MessageSender;
import mana.doodleking.global.swagger.UserRoomControllerDocs;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class UserRoomController implements UserRoomControllerDocs {
    private final UserRoomService userRoomService;
    private final MessageSender messageSender;

    @MessageMapping("/userReady")
    public void setUserReady(@Header("userId") Long userId) {
        UserStateDTO userStateDTO = userRoomService.setUserState(userId, UserState.READY);
        Long roomId = userRoomService.getRoomIdByUserId(userId);
        messageSender.send("/topic/room/" + roomId, userStateDTO);
    }

    @MessageMapping("/userNotReady")
    public void setUserNotReady(@Header("userId") Long userId) {
        UserStateDTO userStateDTO = userRoomService.setUserState(userId, UserState.NOT_READY);
        Long roomId = userRoomService.getRoomIdByUserId(userId);
        messageSender.send("/topic/room/" + roomId, userStateDTO);
    }
}
