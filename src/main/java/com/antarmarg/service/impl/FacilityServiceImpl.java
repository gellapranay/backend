package com.antarmarg.service.impl;

import com.antarmarg.dto.request.CreateEdgeRequest;
import com.antarmarg.dto.request.CreateFacilityRequest;
import com.antarmarg.dto.request.CreateNodeRequest;
import com.antarmarg.dto.response.EdgeDto;
import com.antarmarg.dto.response.FacilityDto;
import com.antarmarg.dto.response.NodeDto;
import com.antarmarg.entity.Facility;
import com.antarmarg.entity.LocationEdge;
import com.antarmarg.entity.LocationNode;
import com.antarmarg.exception.BadRequestException;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.FacilityRepository;
import com.antarmarg.repository.LocationEdgeRepository;
import com.antarmarg.repository.LocationNodeRepository;
import com.antarmarg.service.FacilityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacilityServiceImpl implements FacilityService {

    private final FacilityRepository facilityRepository;
    private final LocationNodeRepository nodeRepository;
    private final LocationEdgeRepository edgeRepository;

    public FacilityServiceImpl(
            FacilityRepository facilityRepository,
            LocationNodeRepository nodeRepository,
            LocationEdgeRepository edgeRepository) {
        this.facilityRepository = facilityRepository;
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacilityDto> getAllFacilities() {
        return facilityRepository.findByIsActiveTrue().stream()
                .map(FacilityDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FacilityDto getFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));
        return FacilityDto.fromEntity(facility);
    }

    @Override
    @Transactional(readOnly = true)
    public FacilityDto getFacilityStructure(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));

        FacilityDto dto = FacilityDto.fromEntity(facility);
        List<LocationNode> nodes = nodeRepository.findByFacilityIdAndIsActiveTrue(id);
        List<LocationEdge> edges = edgeRepository.findByFacilityIdAndIsActiveTrue(id);

        dto.setNodes(nodes.stream().map(NodeDto::fromEntity).collect(Collectors.toList()));
        dto.setEdges(edges.stream().map(EdgeDto::fromEntity).collect(Collectors.toList()));
        return dto;
    }

    @Override
    @Transactional
    public FacilityDto createFacility(CreateFacilityRequest request) {
        Facility facility = new Facility();
        facility.setName(request.getName().trim());
        facility.setFacilityType(request.getFacilityType());
        facility.setAddress(request.getAddress());
        facility.setCity(request.getCity());
        facility.setState(request.getState());
        facility.setCountry(request.getCountry());
        facility.setPostalCode(request.getPostalCode());
        facility.setLatitude(request.getLatitude());
        facility.setLongitude(request.getLongitude());
        facility.setBoundaryJson(request.getBoundaryJson());
        facility.setHierarchySchemaJson(request.getHierarchySchemaJson());
        facility.setIsActive(true);

        Facility saved = facilityRepository.save(facility);
        return FacilityDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public FacilityDto updateFacility(Long id, CreateFacilityRequest request) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));

        facility.setName(request.getName().trim());
        facility.setFacilityType(request.getFacilityType());
        facility.setAddress(request.getAddress());
        facility.setCity(request.getCity());
        facility.setState(request.getState());
        facility.setCountry(request.getCountry());
        facility.setPostalCode(request.getPostalCode());
        if (request.getLatitude() != null) facility.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) facility.setLongitude(request.getLongitude());
        if (request.getBoundaryJson() != null) facility.setBoundaryJson(request.getBoundaryJson());
        if (request.getHierarchySchemaJson() != null) facility.setHierarchySchemaJson(request.getHierarchySchemaJson());

        Facility saved = facilityRepository.save(facility);
        return FacilityDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteFacility(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found with ID: " + id));
        facility.setIsActive(false);
        facilityRepository.save(facility);
    }

    @Override
    @Transactional
    public NodeDto addNode(Long facilityId, CreateNodeRequest request) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found: " + facilityId));

        LocationNode parent = null;
        if (request.getParentNodeId() != null) {
            parent = nodeRepository.findById(request.getParentNodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent node not found: " + request.getParentNodeId()));
        }

        LocationNode node = new LocationNode();
        node.setFacility(facility);
        node.setParentNode(parent);
        node.setName(request.getName().trim());
        node.setNodeType(request.getNodeType());
        node.setLevelNumber(request.getLevelNumber() != null ? request.getLevelNumber() : 0);
        node.setLatitude(request.getLatitude());
        node.setLongitude(request.getLongitude());
        node.setFloorPlanX(request.getFloorPlanX());
        node.setFloorPlanY(request.getFloorPlanY());
        node.setMetadataJson(request.getMetadataJson());
        node.setIsActive(true);

        LocationNode saved = nodeRepository.save(node);
        return NodeDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public NodeDto updateNode(Long facilityId, Long nodeId, CreateNodeRequest request) {
        LocationNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Node not found: " + nodeId));

        if (!node.getFacility().getId().equals(facilityId)) {
            throw new BadRequestException("Node does not belong to facility: " + facilityId);
        }

        if (request.getParentNodeId() != null) {
            LocationNode parent = nodeRepository.findById(request.getParentNodeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Parent node not found: " + request.getParentNodeId()));
            node.setParentNode(parent);
        }

        node.setName(request.getName().trim());
        node.setNodeType(request.getNodeType());
        if (request.getLevelNumber() != null) node.setLevelNumber(request.getLevelNumber());
        if (request.getLatitude() != null) node.setLatitude(request.getLatitude());
        if (request.getLongitude() != null) node.setLongitude(request.getLongitude());
        if (request.getFloorPlanX() != null) node.setFloorPlanX(request.getFloorPlanX());
        if (request.getFloorPlanY() != null) node.setFloorPlanY(request.getFloorPlanY());
        if (request.getMetadataJson() != null) node.setMetadataJson(request.getMetadataJson());

        LocationNode saved = nodeRepository.save(node);
        return NodeDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteNode(Long facilityId, Long nodeId) {
        LocationNode node = nodeRepository.findById(nodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Node not found: " + nodeId));
        node.setIsActive(false);
        nodeRepository.save(node);
    }

    @Override
    @Transactional
    public EdgeDto addEdge(Long facilityId, CreateEdgeRequest request) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ResourceNotFoundException("Facility not found: " + facilityId));

        LocationNode src = nodeRepository.findById(request.getSourceNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Source node not found: " + request.getSourceNodeId()));
        LocationNode dst = nodeRepository.findById(request.getDestinationNodeId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination node not found: " + request.getDestinationNodeId()));

        LocationEdge edge = new LocationEdge();
        edge.setFacility(facility);
        edge.setSourceNode(src);
        edge.setDestinationNode(dst);
        edge.setDistanceMeters(request.getDistanceMeters() != null ? request.getDistanceMeters() : 10.0);
        edge.setEstimatedTimeSeconds(request.getEstimatedTimeSeconds() != null ? request.getEstimatedTimeSeconds() : 15);
        edge.setIsAccessible(request.getIsAccessible() != null ? request.getIsAccessible() : true);
        edge.setAllowVehicles(request.getAllowVehicles() != null ? request.getAllowVehicles() : false);
        edge.setIsBidirectional(request.getIsBidirectional() != null ? request.getIsBidirectional() : true);
        edge.setStatus("OPEN");
        edge.setIsActive(true);

        LocationEdge saved = edgeRepository.save(edge);
        return EdgeDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public EdgeDto updateEdge(Long facilityId, Long edgeId, CreateEdgeRequest request) {
        LocationEdge edge = edgeRepository.findById(edgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Edge not found: " + edgeId));

        if (!edge.getFacility().getId().equals(facilityId)) {
            throw new BadRequestException("Edge does not belong to facility: " + facilityId);
        }

        if (request.getDistanceMeters() != null) edge.setDistanceMeters(request.getDistanceMeters());
        if (request.getEstimatedTimeSeconds() != null) edge.setEstimatedTimeSeconds(request.getEstimatedTimeSeconds());
        if (request.getIsAccessible() != null) edge.setIsAccessible(request.getIsAccessible());
        if (request.getAllowVehicles() != null) edge.setAllowVehicles(request.getAllowVehicles());
        if (request.getIsBidirectional() != null) edge.setIsBidirectional(request.getIsBidirectional());

        LocationEdge saved = edgeRepository.save(edge);
        return EdgeDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public EdgeDto toggleEdgeStatus(Long facilityId, Long edgeId, String status) {
        LocationEdge edge = edgeRepository.findById(edgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Edge not found: " + edgeId));
        edge.setStatus(status.toUpperCase());
        LocationEdge saved = edgeRepository.save(edge);
        return EdgeDto.fromEntity(saved);
    }

    @Override
    @Transactional
    public void deleteEdge(Long facilityId, Long edgeId) {
        LocationEdge edge = edgeRepository.findById(edgeId)
                .orElseThrow(() -> new ResourceNotFoundException("Edge not found: " + edgeId));
        edge.setIsActive(false);
        edgeRepository.save(edge);
    }
}
