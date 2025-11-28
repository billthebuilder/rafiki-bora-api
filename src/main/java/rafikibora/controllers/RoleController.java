package rafikibora.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rafikibora.model.users.Role;
import rafikibora.services.RoleService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/api/roles")
public class RoleController {

    @Autowired
    private RoleService service;

    @PostMapping
    public ResponseEntity<?> addRole(@Valid @RequestBody Role role) {

        Role newRole = service.saveRole(role);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newEmployeeURI = ServletUriComponentsBuilder.fromCurrentRequest() // get the URI for this request
                .path("/{id}") // add to it a path variable
                .buildAndExpand(newRole.getRoleid()) // populate that path variable with the newly created restaurant id
                .toUri(); // convert that work into a human readable URI
        responseHeaders.setLocation(newEmployeeURI); // in the header, set the location location to that URI

        return new ResponseEntity<>(null,
                responseHeaders,
                HttpStatus.CREATED);
    }


    @GetMapping
    public List<Role> findAllRoles() {
        return service.getRoles();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> findRoleById(@PathVariable ("roleId") long roleId) {
        return (ResponseEntity<Role>) service.getRoleById(roleId);
    }

    @GetMapping("/roles/{roleName}")
    public ResponseEntity<Role> findRoleByName(@PathVariable @Valid String roleName) {
        Role r = service.getRoleByName(roleName);
        return new ResponseEntity<>(r, HttpStatus.OK);
    }

    @PatchMapping(value = "/{id}", consumes = {"application/json"})
    public ResponseEntity<?> updateRole(@RequestBody Role roles, @PathVariable int roleId) {
        service.updateRole(roles, roleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable int id) {
        service.deleteRole(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

