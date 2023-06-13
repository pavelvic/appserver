package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.Pool;
import com.darin.appserver.repository.PoolRepository;
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
public class AdminPoolControllerV1 {
    @Autowired
    PoolRepository poolRepository;

    @GetMapping("/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Pool>> getAllPools() {
        List<Pool> pools = new ArrayList<>(poolRepository.findAll());
        if (pools.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pools, HttpStatus.OK);
    }

    @GetMapping("/pools/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Pool> getPoolById(@PathVariable("id") long id) {
        Pool pool = poolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Pool with id = " + id));
        return new ResponseEntity<>(pool, HttpStatus.OK);
    }

    @PostMapping("/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Pool> createPool(@RequestBody Pool pool) {
        Pool _pool = poolRepository.save(new Pool(pool.getName(), pool.getAddress()));
        return new ResponseEntity<>(_pool, HttpStatus.CREATED);
    }

    @PutMapping("/pools/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Pool> updatePool(@PathVariable("id") long id, @RequestBody Pool pool) {
        Pool _pool = poolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found Pool with id = " + id));
        _pool.setName(pool.getName());
        _pool.setAddress(pool.getAddress());
        return new ResponseEntity<>(poolRepository.save(_pool), HttpStatus.OK);
    }

    @DeleteMapping("/pools/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deletePool(@PathVariable("id") long id) {
        poolRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllPools() {
        poolRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}