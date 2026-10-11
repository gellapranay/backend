package com.antarmarg.service;

import com.antarmarg.dto.request.CreateEdgeRequest;
import com.antarmarg.dto.request.CreateFacilityRequest;
import com.antarmarg.dto.request.CreateNodeRequest;
import com.antarmarg.dto.response.EdgeDto;
import com.antarmarg.dto.response.FacilityDto;
import com.antarmarg.dto.response.NodeDto;

import java.util.List;

public interface FacilityService {
    List<FacilityDto> getAllFacilities();
    FacilityDto getFacilityById(Long id);
    FacilityDto getFacilityStructure(Long id);
    FacilityDto createFacility(CreateFacilityRequest request);
    FacilityDto updateFacility(Long id, CreateFacilityRequest request);
    void deleteFacility(Long id);

    NodeDto addNode(Long facilityId, CreateNodeRequest request);
    NodeDto updateNode(Long facilityId, Long nodeId, CreateNodeRequest request);
    void deleteNode(Long facilityId, Long nodeId);

    EdgeDto addEdge(Long facilityId, CreateEdgeRequest request);
    EdgeDto updateEdge(Long facilityId, Long edgeId, CreateEdgeRequest request);
    EdgeDto toggleEdgeStatus(Long facilityId, Long edgeId, String status);
    void deleteEdge(Long facilityId, Long edgeId);
}
