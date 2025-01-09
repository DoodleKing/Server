package mana.doodleking.domain.user;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "`character`")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageURL;
    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> userList;
}