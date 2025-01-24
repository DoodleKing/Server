package mana.doodleking.domain.user.repository;

import mana.doodleking.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
    default User findByIdOrThrow(Long id) {
        return findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자 id: " + id));
    }
}
