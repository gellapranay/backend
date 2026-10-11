package com.antarmarg.repository;

import com.antarmarg.entity.LocationNode;
import com.antarmarg.entity.NodeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationNodeRepository extends JpaRepository<LocationNode, Long> {
    List<LocationNode> findByFacilityIdAndIsActiveTrue(Long facilityId);
    List<LocationNode> findByFacilityId(Long facilityId);
    List<LocationNode> findByFacilityIdAndNodeType(Long facilityId, NodeType nodeType);
    List<LocationNode> findByParentNodeId(Long parentNodeId);
    List<LocationNode> findByFacilityIdAndParentNodeIsNull(Long facilityId);
}
