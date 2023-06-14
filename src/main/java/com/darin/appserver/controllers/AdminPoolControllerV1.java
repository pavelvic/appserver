package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.Pool;
import com.darin.appserver.models.RoleCrud;
import com.darin.appserver.models.UserCrud;
import com.darin.appserver.repository.PoolRepository;
import com.darin.appserver.repository.RoleCrudRepository;
import com.darin.appserver.repository.UserCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
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
    private PoolRepository poolRepository;
    @Autowired
    private UserCrudRepository userCrudRepository;

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

    @GetMapping("/users/{userId}/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Pool>> getAllPoolsByUserId(@PathVariable(value = "userId") Long userId) {
        if (!userCrudRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Not found UserCrud with id = " + userId);
        }
        List<Pool> pools = poolRepository.findPoolsByUserCrudsId(userId);
        return new ResponseEntity<>(pools, HttpStatus.OK);
    }

    @GetMapping("/pools/{poolId}/users")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<UserCrud>> getAllUsersByPoolId(@PathVariable(value = "poolId") Long poolId) {
        if (!poolRepository.existsById(poolId)) {
            throw new ResourceNotFoundException("Not found Pool with id = " + poolId);
        }
        List<UserCrud> userCruds = userCrudRepository.findUsersByPoolsId(poolId);
        return new ResponseEntity<>(userCruds, HttpStatus.OK);
    }

    @PostMapping("/users/{userId}/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Pool> addPool(@PathVariable(value = "userId") Long userId, @RequestBody Pool poolRequest) {
        Pool pool = userCrudRepository.findById(userId).map(userCrud -> {
            long poolId = poolRequest.getId();
            // roleCrud is existed
            if (poolId != 0L) {
                Pool _pool = poolRepository.findById(poolId).orElseThrow(() -> new ResourceNotFoundException("Not found Pool with id = " + poolId));
                userCrud.addPool(_pool);
                userCrudRepository.save(userCrud);
                return _pool;
            }
            // add and create new Pool
            userCrud.addPool(poolRequest);
            return poolRepository.save(poolRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
        return new ResponseEntity<>(pool, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{userId}/pools/{poolId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deletePoolFromUser(@PathVariable(value = "userId") Long userId, @PathVariable(value = "poolId") Long poolId) {
        UserCrud userCrud = userCrudRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Not found User with id = " + userId));
        userCrud.removePool(poolId);
        userCrudRepository.save(userCrud);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}