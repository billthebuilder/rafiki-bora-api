package rafikibora.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rafikibora.model.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(long id);

    Optional<User> findByMid(String mid);

    User findByEmail(String email);

    List<User> findByRoles_Role_RoleNameContainingIgnoreCase(String roleName);

    // Lists all terminals that:
    // - Belong to a merchant with merchant ID 'mid'
    // - Are not assigned to an agent
    List<User> findByUserMakerAndAssignedTerminalsIsNull(User merchant);


}
