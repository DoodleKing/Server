package mana.doodleking.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import mana.doodleking.domain.user.domain.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {
    boolean existsById(Long id);
    Character findCharacterById(Long id);
}
