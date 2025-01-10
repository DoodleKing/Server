package mana.doodleking.domain.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mana.doodleking.domain.room.dto.CreateRoomRes;
import mana.doodleking.domain.room.dto.PostRoomReq;

import java.util.List;

@Entity
@Getter
@Table(name = "room")
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long player;
    private String password;
    private Long time;
    private Long round;
    private Boolean hint;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRoomList;

    @Builder
    private Room(String name, Subject subject, RoomState roomState, Long player, String password, Long time, Long round, Boolean hint) {
        this.name = name;
        this.subject = subject;
        this.roomState = roomState;
        this.player = player;
        this.password = password;
        this.time = time;
        this.round = round;
        this.hint = hint;
    }

    public static Room from(PostRoomReq postRoomReq) {
        return Room.builder()
            .name(postRoomReq.getName())
            .subject(postRoomReq.getSubject())
            .roomState(postRoomReq.getRoomState())
            .player(postRoomReq.getPlayer())
            .password(postRoomReq.getPassword())
            .time(postRoomReq.getTime())
            .round(postRoomReq.getRound())
            .hint(postRoomReq.getHint())
            .build();
    }
}
