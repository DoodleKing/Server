package mana.doodleking.domain.room;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
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
}
