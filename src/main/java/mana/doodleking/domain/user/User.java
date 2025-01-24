package mana.doodleking.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mana.doodleking.domain.room.UserRoom;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "`user`")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDateTime lastActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRoom> userRoomList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Builder
    private User(String userName, Character character) {
        this.name = userName;
        this.character = character;
        this.lastActive = LocalDateTime.now();
    }

    public static User of(String userName, Character character) {
        return User.builder()
                .userName(userName)
                .character(character)
                .build();
    }
}
