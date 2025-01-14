package mana.doodleking.websocket;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestChatContentsDto {
    private MessageType type;
    private String contents;
}
