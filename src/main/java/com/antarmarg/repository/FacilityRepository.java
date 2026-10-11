package com.antarmarg.repository;

import com.antarmarg.entity.Facility;
import com.antarmarg.entity.FacilityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Long> {
    List<Facility> findByIsActiveTrue();
    List<Facility> findByFacilityType(FacilityType facilityType);
    long countByIsActiveTrue();
}
