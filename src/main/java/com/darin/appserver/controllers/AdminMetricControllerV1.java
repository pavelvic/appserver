package com.darin.appserver.controllers;

import com.darin.appserver.exception.ResourceNotFoundException;
import com.darin.appserver.models.Metric;
import com.darin.appserver.models.Sensor;
import com.darin.appserver.repository.MetricRepository;
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
public class AdminMetricControllerV1 {
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MetricRepository metricRepository;

    @GetMapping("/metrics")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Metric>> getAllMetrics() {
        List<Metric> metrics = new ArrayList<>(metricRepository.findAll());
        if (metrics.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }

    @GetMapping("/sensors/{sensorId}/metrics")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Metric>> getAllMetricsBySensorId(@PathVariable(value = "sensorId") Long sensorId) {
        if (!sensorRepository.existsById(sensorId)) {
            throw new ResourceNotFoundException("Not found Sensor with id = " + sensorId);
        }
        List<Metric> metrics = metricRepository.findMetricsBySensorsId(sensorId);
        return new ResponseEntity<>(metrics, HttpStatus.OK);
    }

    @GetMapping("/metrics/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Metric> getMetricsById(@PathVariable(value = "id") Long id) {
        Metric metric = metricRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Not found Metric with id = " + id));
        return new ResponseEntity<>(metric, HttpStatus.OK);
    }

    @GetMapping("/metrics/{metricId}/sensors")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Sensor>> getAllSensorsByMetricId(@PathVariable(value = "metricId") Long metricId) {
        if (!metricRepository.existsById(metricId)) {
            throw new ResourceNotFoundException("Not found Metric with id = " + metricId);
        }
        List<Sensor> sensors = sensorRepository.findSensorsByMetricsId(metricId);
        return new ResponseEntity<>(sensors, HttpStatus.OK);
    }

    @PostMapping("/sensors/{sensorId}/metrics")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Metric> addMetric(@PathVariable(value = "sensorId") Long sensorId, @RequestBody Metric metricRequest) {
        Metric metric = sensorRepository.findById(sensorId).map(sensor -> {
            long metricId = metricRequest.getId();
            // metric is existed
            if (metricId != 0L) {
                Metric _metric = metricRepository.findById(metricId).orElseThrow(() -> new ResourceNotFoundException("Not found Metric with id = " + metricId));
                sensor.addMetric(_metric);
                sensorRepository.save(sensor);
                return _metric;
            }
            // add and create new RoleCrud
            sensor.addMetric(metricRequest);
            return metricRepository.save(metricRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Not found Sensor with id = " + sensorId));
        return new ResponseEntity<>(metric, HttpStatus.CREATED);
    }

    @PutMapping("/metrics/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Metric> updateMetric(@PathVariable("id") long id, @RequestBody Metric metricRequest) {
        Metric metric = metricRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MetricId " + id + "not found"));
        metric.setName(metricRequest.getName());
        metric.setDescription(metricRequest.getDescription());
        metric.setCriticalMinValue(metricRequest.getCriticalMinValue());
        metric.setCriticalMaxValue(metricRequest.getCriticalMaxValue());
        return new ResponseEntity<>(metricRepository.save(metric), HttpStatus.OK);
    }

    @DeleteMapping("/sensors/{sensorId}/metrics/{metricId}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteMetricFromSensor(@PathVariable(value = "sensorId") Long sensorId, @PathVariable(value = "metricId") Long metricId) {
        Sensor sensor = sensorRepository.findById(sensorId).orElseThrow(() -> new ResourceNotFoundException("Not found Sensor with id = " + sensorId));
        sensor.removeMetric(metricId);
        sensorRepository.save(sensor);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/metrics/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteMetric(@PathVariable("id") long id) {
        metricRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}