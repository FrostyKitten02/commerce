package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.afridau.commerce.auth.model.Group;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepo extends JpaRepository<Group, UUID> {
    Optional<Group> findByName(String name);
}
