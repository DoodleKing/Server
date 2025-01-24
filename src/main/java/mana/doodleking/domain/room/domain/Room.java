package mana.doodleking.domain.room.domain;

import jakarta.persistence.*;
import lombok.*;
import mana.doodleking.domain.room.dto.PostRoomReq;
import mana.doodleking.domain.room.dto.UpdateRoomReq;
import mana.doodleking.domain.room.enums.RoomState;
import mana.doodleking.domain.room.enums.Subject;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "room")
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public Room update(UpdateRoomReq updateRoomReq) {
        return Room.builder()
            .id(this.id)
            .name(updateRoomReq.getName() != null ? updateRoomReq.getName() : this.name)
            .subject(updateRoomReq.getSubject() != null ? updateRoomReq.getSubject() : this.subject)
            .maxPlayer(updateRoomReq.getMaxPlayer() != null ? updateRoomReq.getMaxPlayer() : this.maxPlayer)
            .password(updateRoomReq.getPassword() != null ? updateRoomReq.getPassword() : this.password)
            .time(updateRoomReq.getTime() != null ? updateRoomReq.getTime() : this.time)
            .round(updateRoomReq.getRound() != null ? updateRoomReq.getRound() : this.round)
            .hint(updateRoomReq.getHint() != null ? updateRoomReq.getHint() : this.hint)
            .roomState(this.roomState)
            .curPlayer(this.curPlayer)
            .userRoomList(this.userRoomList)
            .version(this.version)
            .build();
    }
}
