package mana.doodleking.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpMessagingTemplate template;
    @Autowired
    public WebSocketController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/test") // 메세지 전송
    public void sendMessage(RequestChatContentsDto message) throws Exception {
        Thread.sleep(1000); // simulated delay
        template.convertAndSend("/sub", message); // 구독한 채팅방으로 메세지 전송
    }
}
