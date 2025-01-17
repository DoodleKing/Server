package mana.doodleking.global;

import lombok.AllArgsConstructor;
import mana.doodleking.global.response.APIResponse;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MessageSender {
    private final SimpMessagingTemplate messagingTemplate;

    public <T> void send(String dest, T data) {
        messagingTemplate.convertAndSend(dest, APIResponse.success(data));
    }
}
