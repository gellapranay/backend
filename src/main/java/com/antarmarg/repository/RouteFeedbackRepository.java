package com.antarmarg.repository;

import com.antarmarg.entity.RouteFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteFeedbackRepository extends JpaRepository<RouteFeedback, Long> {
    List<RouteFeedback> findByDeliveryId(Long deliveryId);
    List<RouteFeedback> findByUserId(Long userId);
}
