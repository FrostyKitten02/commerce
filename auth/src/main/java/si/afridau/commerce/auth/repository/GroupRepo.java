package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.afridau.commerce.auth.model.Role;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepo extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
