package com.antarmarg.dto.response;

import com.antarmarg.entity.Facility;
import com.antarmarg.entity.FacilityType;

import java.util.List;

public class FacilityDto {
    private Long id;
    private String name;
    private FacilityType facilityType;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private Double latitude;
    private Double longitude;
    private String boundaryJson;
    private String hierarchySchemaJson;
    private Boolean isActive;
    private List<NodeDto> nodes;
    private List<EdgeDto> edges;

    public FacilityDto() {
    }

    public static FacilityDto fromEntity(Facility facility) {
        if (facility == null) return null;
        FacilityDto dto = new FacilityDto();
        dto.setId(facility.getId());
        dto.setName(facility.getName());
        dto.setFacilityType(facility.getFacilityType());
        dto.setAddress(facility.getAddress());
        dto.setCity(facility.getCity());
        dto.setState(facility.getState());
        dto.setCountry(facility.getCountry());
        dto.setPostalCode(facility.getPostalCode());
        dto.setLatitude(facility.getLatitude());
        dto.setLongitude(facility.getLongitude());
        dto.setBoundaryJson(facility.getBoundaryJson());
        dto.setHierarchySchemaJson(facility.getHierarchySchemaJson());
        dto.setIsActive(facility.getIsActive());
        return dto;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FacilityType getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(FacilityType facilityType) {
        this.facilityType = facilityType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getBoundaryJson() {
        return boundaryJson;
    }

    public void setBoundaryJson(String boundaryJson) {
        this.boundaryJson = boundaryJson;
    }

    public String getHierarchySchemaJson() {
        return hierarchySchemaJson;
    }

    public void setHierarchySchemaJson(String hierarchySchemaJson) {
        this.hierarchySchemaJson = hierarchySchemaJson;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public List<NodeDto> getNodes() {
        return nodes;
    }

    public void setNodes(List<NodeDto> nodes) {
        this.nodes = nodes;
    }

    public List<EdgeDto> getEdges() {
        return edges;
    }

    public void setEdges(List<EdgeDto> edges) {
        this.edges = edges;
    }
}
