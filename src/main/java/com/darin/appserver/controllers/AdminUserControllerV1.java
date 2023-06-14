package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.UserCrud;
import com.darin.appserver.repository.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/admin/")
public class AdminUserControllerV1 {
    @Autowired
    UserCrudRepository userCrudRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<UserCrud>> getAllUsers() {
        List<UserCrud> userCruds = new ArrayList<>(userCrudRepository.findAll());
        if (userCruds.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(userCruds, HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserCrud> getUserById(@PathVariable("userId") long id) {
        UserCrud userCrud = userCrudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));
        return new ResponseEntity<>(userCrud, HttpStatus.OK);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserCrud> createUser(@RequestBody UserCrud userCrud) {
        UserCrud _userCrud = userCrudRepository.save(new UserCrud(userCrud.getUsername(), userCrud.getEmail(), passwordEncoder.encode(userCrud.getPassword())));
        return new ResponseEntity<>(_userCrud, HttpStatus.CREATED);
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<UserCrud> updateUser(@PathVariable("userId") long id, @RequestBody UserCrud userCrud) {
        UserCrud _userCrud = userCrudRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + id));
        _userCrud.setUsername(userCrud.getUsername());
        _userCrud.setEmail(userCrud.getEmail());
        _userCrud.setPassword(userCrud.getPassword());
        return new ResponseEntity<>(userCrudRepository.save(_userCrud), HttpStatus.OK);
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("userId") long id) {
        userCrudRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        userCrudRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}