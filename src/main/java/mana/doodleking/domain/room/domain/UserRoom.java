package mana.doodleking.domain.room.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mana.doodleking.domain.user.domain.User;
import mana.doodleking.domain.user.enums.UserRole;
import mana.doodleking.domain.user.enums.UserState;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "user_room")
public class UserRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UserRole role;
    private UserState state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Builder
    private UserRoom(UserRole role, UserState state, User user, Room room) {
        this.role = role;
        this.state = state;
        this.user = user;
        this.room = room;
    }

    public static UserRoom of(UserRole role, User user, Room room) {
        return UserRoom.builder()
                .role(role)
                .state(UserState.NOT_READY)
                .user(user)
                .room(room)
                .build();
    }
}
