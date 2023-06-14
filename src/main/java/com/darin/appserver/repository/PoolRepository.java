package com.darin.appserver.repository;

import com.darin.appserver.models.Pool;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PoolRepository extends JpaRepository<Pool, Long> {
    List<Pool> findPoolsBySensorsId(Long sensorId);
    List<Pool> findPoolsByUserCrudsId(Long userId);
}
