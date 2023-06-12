package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.RoleCrud;
import com.darin.appserver.models.UserCrud;
import com.darin.appserver.repository.RoleCrudRepository;
import com.darin.appserver.repository.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/admin/")
public class AdminRoleControllerV1 {
    @Autowired
    private UserCrudRepository userCrudRepository;

    @Autowired
    private RoleCrudRepository roleCrudRepository;

    @GetMapping("/roles")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<RoleCrud>> getAllRoles() {
        List<RoleCrud> roleCruds = new ArrayList<>(roleCrudRepository.findAll());
        if (roleCruds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(roleCruds, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<RoleCrud>> getAllRolesByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userCrudRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found UserCrud with id = " + userId);
        }
        List<RoleCrud> roleCruds = roleCrudRepository.findRoleCrudsByUserCrudsId(userId);
        return new ResponseEntity<>(roleCruds, HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<RoleCrud> getRolesById(@PathVariable(value = "id") Long id) {
        RoleCrud roleCrud = roleCrudRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Role with id = " + id));
        return new ResponseEntity<>(roleCrud, HttpStatus.OK);
    }

    @GetMapping("/roles/{roleId}/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<UserCrud>> getAllUsersByRoleId(@PathVariable(value = "roleId") Long roleId) {
        if (!roleCrudRepository.existsById(roleId)) {
            throw new ResourceNotFoundException("Not found Role  with id = " + roleId);
        }
        List<UserCrud> userCruds = userCrudRepository.findUsersByRoleCrudsId(roleId);
        return new ResponseEntity<>(userCruds, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<RoleCrud> addRole(@PathVariable(value = "userId") Long userId, @RequestBody RoleCrud roleCrudRequest) {
        RoleCrud roleCrud = userCrudRepository.findById(userId).map(userCrud -> {
            long roleId = roleCrudRequest.getId();
            // roleCrud is existed
            if (roleId != 0L) {
                RoleCrud _roleCrud = roleCrudRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Not found Role with id = " + roleId));
                userCrud.addRoleCrud(_roleCrud);
                userCrudRepository.save(userCrud);
                return _roleCrud;
            }
            // add and create new RoleCrud
            userCrud.addRoleCrud(roleCrudRequest);
            return roleCrudRepository.save(roleCrudRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
        return new ResponseEntity<>(roleCrud, HttpStatus.CREATED);
    }

    @PutMapping("/roles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<RoleCrud> updateRole(@PathVariable("id") long id, @RequestBody RoleCrud roleCrudRequest) {
        RoleCrud roleCrud = roleCrudRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("RoleId " + id + "not found"));
        roleCrud.setName(roleCrudRequest.getName());
        return new ResponseEntity<>(roleCrudRepository.save(roleCrud), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}/roles/{roleId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteRoleFromUser(@PathVariable(value = "userId") Long userId, @PathVariable(value = "roleId") Long roleId) {
        UserCrud userCrud = userCrudRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
        userCrud.removeRoleCrud(roleId);
        userCrudRepository.save(userCrud);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteRole(@PathVariable("id") long id) {
        roleCrudRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}