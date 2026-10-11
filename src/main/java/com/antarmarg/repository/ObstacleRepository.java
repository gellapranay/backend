package com.antarmarg.repository;

import com.antarmarg.entity.Obstacle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObstacleRepository extends JpaRepository<Obstacle, Long> {
    List<Obstacle> findByFacilityIdAndStatus(Long facilityId, String status);
    List<Obstacle> findByFacilityIdOrderByCreatedAtDesc(Long facilityId);
    List<Obstacle> findByStatusOrderByCreatedAtDesc(String status);
    long countByStatus(String status);
}
