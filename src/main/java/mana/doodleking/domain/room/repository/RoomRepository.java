package mana.doodleking.domain.room.repository;

import mana.doodleking.domain.room.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    default Room findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 방 id: " + id));
    }
}
