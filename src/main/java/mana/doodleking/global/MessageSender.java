package mana.doodleking.global;

import lombok.AllArgsConstructor;
import mana.doodleking.global.response.APIResponse;
import mana.doodleking.global.response.ResultCode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageSender {
    private final SimpMessagingTemplate messagingTemplate;

    public <T> void send(String dest, T data) {
        messagingTemplate.convertAndSend(dest, APIResponse.success(data));
    }
    public void sendError(String dest, String message) {
        messagingTemplate.convertAndSend(dest, APIResponse.failure(ResultCode.BAD_REQUEST, message));
    }
}
