package mana.doodleking.domain.user.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "`character`")
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String imageURL;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> userList;

    @Builder
    private Character(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    public static Character of(String name, String imageURL) {
        return Character.builder()
                .name(name)
                .imageURL(imageURL)
                .build();
    }
}