package mana.doodleking.domain.room.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "room")
@NoArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Subject subject;
    private RoomState roomState;
    private Long maxPlayer;
    private Long curPlayer;
    private String password;
    private Long time;
    private Long round;
    private Boolean hint;
    @Version
    private Long version;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRoomList;

    @Builder
    private Room(String name, Subject subject, RoomState roomState, Long maxPlayer, Long curPlayer, String password, Long time, Long round, Boolean hint) {
        this.name = name;
        this.subject = subject;
        this.roomState = roomState;
        this.maxPlayer = maxPlayer;
        this.curPlayer = curPlayer;
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
            .maxPlayer(postRoomReq.getMaxPlayer())
            .curPlayer(1L)
            .password(postRoomReq.getPassword())
            .time(postRoomReq.getTime())
            .round(postRoomReq.getRound())
            .hint(postRoomReq.getHint())
            .build();
    }
}
