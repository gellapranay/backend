package com.antarmarg.repository;

import com.antarmarg.entity.Delivery;
import com.antarmarg.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByTrackingNumber(String trackingNumber);
    List<Delivery> findByCustomerIdOrderByCreatedAtDesc(Long customerId);
    List<Delivery> findByDeliveryPartnerIdOrderByCreatedAtDesc(Long deliveryPartnerId);
    List<Delivery> findByFacilityIdOrderByCreatedAtDesc(Long facilityId);
    List<Delivery> findByStatusOrderByCreatedAtDesc(DeliveryStatus status);
    List<Delivery> findByStatusInOrderByCreatedAtDesc(List<DeliveryStatus> statuses);

    long countByStatus(DeliveryStatus status);

    @Query("SELECT AVG(d.actualDurationSeconds) FROM Delivery d WHERE d.status = 'COMPLETED' AND d.actualDurationSeconds IS NOT NULL")
    Double findAverageCompletedDurationSeconds();

    @Query("SELECT AVG(d.totalDistanceMeters) FROM Delivery d WHERE d.totalDistanceMeters IS NOT NULL")
    Double findAverageDistanceMeters();
}
