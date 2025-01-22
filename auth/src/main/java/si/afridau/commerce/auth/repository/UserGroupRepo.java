package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import si.afridau.commerce.auth.model.UserRole;

import java.util.UUID;

@Repository
public interface UserGroupRepo extends JpaRepository<UserRole, UUID> {
}
