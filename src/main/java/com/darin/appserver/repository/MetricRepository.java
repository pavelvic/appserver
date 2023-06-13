package com.darin.appserver.repository;

import com.darin.appserver.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MetricRepository extends JpaRepository<Metric, Long> {
    List<Metric> findMetricsBySensorsId(Long sensorId);
}
