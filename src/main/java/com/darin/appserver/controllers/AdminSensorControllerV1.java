package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.Pool;
import com.darin.appserver.models.Sensor;
import com.darin.appserver.repository.PoolRepository;
import com.darin.appserver.repository.SensorRepository;
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
public class AdminSensorControllerV1 {
    @Autowired
    private PoolRepository poolRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @GetMapping("/sensors")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Sensor>> getAllSensors() {
        List<Sensor> sensors = new ArrayList<>(sensorRepository.findAll());
        if (sensors.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    @GetMapping("/pools/{poolId}/sensors")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Sensor>> getAllSensorsByPoolId(@PathVariable(value = "poolId") Long poolId) {
        if (!poolRepository.existsById(poolId)) {
            throw new ResourceNotFoundException("Not found Pool with id = " + poolId);
        }
        List<Sensor> sensors = sensorRepository.findSensorsByPoolsId(poolId);
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    @GetMapping("/sensors/{sensorId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Sensor> getSensorById(@PathVariable(value = "sensorId") Long id) {
        Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Sensor with id = " + id));
        return new ResponseEntity<>(sensor, HttpStatus.OK);
    }

    @GetMapping("/sensors/{sensorId}/pools")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Pool>> getAllPoolsBySensorId(@PathVariable(value = "sensorId") Long sensorId) {
        if (!sensorRepository.existsById(sensorId)) {
            throw new ResourceNotFoundException("Not found Sensor  with id = " + sensorId);
        }
        List<Pool> pools = poolRepository.findPoolsBySensorsId(sensorId);
        return new ResponseEntity<>(pools, HttpStatus.OK);
    }

    @PostMapping("/pools/{poolId}/sensors")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Sensor> addSensor(@PathVariable(value = "poolId") Long poolId, @RequestBody Sensor sensorRequest) {
        Sensor sensor = poolRepository.findById(poolId).map(pool -> {
            long sensorId = sensorRequest.getId();
            // sensor is existed
            if (sensorId != 0L) {
                Sensor _sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new ResourceNotFoundException("Not found Sensor with id = " + sensorId));
                pool.addSensor(_sensor);
                poolRepository.save(pool);
                return _sensor;
            }
            // add and create new Sensor
            pool.addSensor(sensorRequest);
            return sensorRepository.save(sensorRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Pool with id = " + poolId));
        return new ResponseEntity<>(sensor, HttpStatus.CREATED);
    }

    @PutMapping("/sensors/{sensorId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Sensor> updateSensor(@PathVariable("sensorId") long id, @RequestBody Sensor sensorRequest) {
        Sensor sensor = sensorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("SensorId " + id + "not found"));
        sensor.setName(sensorRequest.getName());
        sensor.setDescription(sensorRequest.getDescription());
        return new ResponseEntity<>(sensorRepository.save(sensor), HttpStatus.OK);
    }

    @DeleteMapping("/pools/{poolId}/sensors/{sensorId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteSensorFromPool(@PathVariable(value = "poolId") Long poolId, @PathVariable(value = "sensorId") Long sensorId) {
        Pool pool = poolRepository.findById(poolId).orElseThrow(() -> new ResourceNotFoundException("Not found Pool with id = " + poolId));
        pool.removeSensor(sensorId);
        poolRepository.save(pool);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/sensors/{sensorId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteSensor(@PathVariable("sensorId") long id) {
        sensorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}