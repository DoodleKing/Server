package mana.doodleking.domain.room.repository;

import mana.doodleking.domain.room.domain.UserRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
}
