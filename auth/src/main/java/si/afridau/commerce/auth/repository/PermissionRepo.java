package si.afridau.commerce.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import si.afridau.commerce.auth.model.Permission;

import java.util.List;
import java.util.UUID;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, UUID> {
    @Query("SELECT p FROM Permission p " +
            "JOIN RolePermission rp ON p.id = rp.permissionId " +
            "JOIN UserRole ur ON ur.roleId = rp.roleId " +
            "WHERE ur.userId = :userId")
    List<Permission> getRolesByUserId(UUID userId);
}
