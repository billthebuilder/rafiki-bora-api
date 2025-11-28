package rafikibora.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rafikibora.dto.Response;
import rafikibora.exceptions.ResourceNotFoundException;
import rafikibora.model.users.Role;
import rafikibora.repository.RoleRepository;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    @Transactional
    public Role saveRole(Role role) {
        return repository.save(role);
    }

    public List<Role> getRoles() {
        return repository.findAll();
    }

    public ResponseEntity<?> getRoleById(long roleId) {
        Response response;
        Optional<Role> optional = repository.findById(roleId);
        Role roles = null;
        if (optional.isPresent()) {
            roles = optional.get();
        } else {
            response = new Response(Response.responseStatus.FAILED," Account not found for id :: " + roleId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    public Role getRoleByName(String roleName ) {
        return repository.findByRoleName(roleName);
    }

    @Transactional
    public void deleteRole(long id) {
        if ( repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Roles " + id + " Not Found");
        }
    }

    @Transactional
    public Role updateRole(Role roles, long roleid) {
        Role existingRoles = repository.findById(roleid).
                orElseThrow(
                        () -> new ResourceNotFoundException
                                ("Roles " + roleid + " Not Found"));

        if (roles.getRoleName() != null) {
            existingRoles.setRoleName(roles.getRoleName());
        }

        return repository.save(existingRoles);
    }

}
