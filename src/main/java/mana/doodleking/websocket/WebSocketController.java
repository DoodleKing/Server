package mana.doodleking.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/test")
    @SendTo("/topic")
    public RequestChatContentsDto sendMessage(RequestChatContentsDto message) throws Exception {
        Thread.sleep(1000);
        message.setContents(message.getContents() + " test");
        return message;
    }
}
