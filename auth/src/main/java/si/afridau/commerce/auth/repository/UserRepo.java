package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import si.afridau.commerce.auth.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
