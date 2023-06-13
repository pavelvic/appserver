package com.darin.appserver.repository;

import com.darin.appserver.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    List<Sensor> findSensorsByPoolsId(Long poolId);

    List<Sensor> findSensorsByMetricsId(Long metricId);
}
