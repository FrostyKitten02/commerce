package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.afridau.commerce.auth.model.Role;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepo extends JpaRepository<Role, UUID> {
    @Query("SELECT r FROM Role r " +
            "JOIN GroupRole gr ON r.id = gr.roleId " +
            "JOIN UserGroup ug ON gr.groupId = ug.groupId " +
            "WHERE ug.userId = :userId")
    List<Role> getRolesByUserId(UUID userId);
}
