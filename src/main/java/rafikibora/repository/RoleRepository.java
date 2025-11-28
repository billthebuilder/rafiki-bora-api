package rafikibora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rafikibora.model.users.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
    Role findByRoleName(String roleName);
}
