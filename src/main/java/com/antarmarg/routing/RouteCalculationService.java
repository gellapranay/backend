package com.antarmarg.routing;

import com.antarmarg.dto.request.RouteRecommendationRequest;
import com.antarmarg.dto.response.*;
import com.antarmarg.entity.*;
import com.antarmarg.exception.ResourceNotFoundException;
import com.antarmarg.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouteCalculationService {

    private final LocationNodeRepository nodeRepository;
    private final LocationEdgeRepository edgeRepository;
    private final ObstacleRepository obstacleRepository;
    private final GateSelectionService gateSelectionService;
    private final DijkstraRouter dijkstraRouter;

    public RouteCalculationService(
            LocationNodeRepository nodeRepository,
            LocationEdgeRepository edgeRepository,
            ObstacleRepository obstacleRepository,
            GateSelectionService gateSelectionService,
            DijkstraRouter dijkstraRouter) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
        this.obstacleRepository = obstacleRepository;
        this.gateSelectionService = gateSelectionService;
        this.dijkstraRouter = dijkstraRouter;
    }

    public RouteCalculationResponse calculateRecommendedRoute(RouteRecommendationRequest request) {
        Long facilityId = request.getFacilityId();
        Long destinationId = request.getDestinationNodeId();

        LocationNode destinationNode = nodeRepository.findById(destinationId)
                .orElseThrow(() -> new ResourceNotFoundException("Destination node not found: " + destinationId));

        if (!destinationNode.getFacility().getId().equals(facilityId)) {
            return RouteCalculationResponse.notFound("Selected destination does not belong to facility: " + facilityId);
        }

        // Fetch all edges for the facility
        List<LocationEdge> allEdges = edgeRepository.findByFacilityIdAndIsActiveTrue(facilityId);

        // Fetch active obstacles for facility
        List<Obstacle> activeObstacles = obstacleRepository.findByFacilityIdAndStatus(facilityId, "ACTIVE");
        Set<Long> blockedEdgeIds = new HashSet<>();
        Set<Long> blockedNodeIds = new HashSet<>();
        List<String> obstacleDescriptions = new ArrayList<>();

        for (Obstacle obs : activeObstacles) {
            if (obs.getLocationEdge() != null) {
                blockedEdgeIds.add(obs.getLocationEdge().getId());
                obstacleDescriptions.add(obs.getObstacleType() + ": " + (obs.getDescription() != null ? obs.getDescription() : "Blocked edge"));
            }
            if (obs.getLocationNode() != null) {
                blockedNodeIds.add(obs.getLocationNode().getId());
                obstacleDescriptions.add(obs.getObstacleType() + " at " + obs.getLocationNode().getName() + ": " + (obs.getDescription() != null ? obs.getDescription() : "Blocked node"));
            }
        }

        // Evaluate Gates
        boolean requireAccessible = Boolean.TRUE.equals(request.getRequireAccessible());
        boolean isVehicle = Boolean.TRUE.equals(request.getIsVehicle());

        List<GateSelectionService.EvaluatedGate> evaluatedGates = gateSelectionService.evaluateGates(
                facilityId,
                destinationNode,
                allEdges,
                blockedEdgeIds,
                blockedNodeIds,
                requireAccessible,
                isVehicle,
                request.getPreferredGateId()
        );

        if (evaluatedGates.isEmpty()) {
            return RouteCalculationResponse.notFound(
                    "No reachable route found from any facility entry gate to destination '" +
                            destinationNode.getName() + "'. Active obstacles or access restrictions may have disconnected the path."
            );
        }

        GateSelectionService.EvaluatedGate bestGate = evaluatedGates.get(0);
        DijkstraRouter.PathResult primaryPath = bestGate.getPathResult();

        RouteCalculationResponse response = new RouteCalculationResponse();
        response.setRouteFound(true);
        response.setMessage("Optimal last-meter route computed successfully.");
        response.setRecommendedGate(NodeDto.fromEntity(bestGate.getGateNode()));
        response.setTotalDistanceMeters(primaryPath.getTotalDistance());
        response.setEstimatedTimeSeconds(primaryPath.getTotalTimeSeconds());
        response.setIsAccessible(requireAccessible);
        response.setObstaclesAvoided(obstacleDescriptions);

        // Map path nodes and edges to DTOs
        List<NodeDto> pathNodeDtos = primaryPath.getNodes().stream()
                .map(NodeDto::fromEntity)
                .collect(Collectors.toList());
        response.setPathNodes(pathNodeDtos);

        List<EdgeDto> pathEdgeDtos = primaryPath.getEdges().stream()
                .map(EdgeDto::fromEntity)
                .collect(Collectors.toList());
        response.setPathEdges(pathEdgeDtos);

        // Generate Turn-by-Turn Navigation Instructions
        response.setInstructions(generateInstructions(primaryPath.getNodes(), primaryPath.getEdges()));

        // Generate Rationale
        StringBuilder rationale = new StringBuilder();
        rationale.append("Recommended ").append(bestGate.getGateNode().getName())
                .append(" as optimal entrance: shortest internal pathway (")
                .append(String.format("%.0f", primaryPath.getTotalDistance())).append("m, ~")
                .append(primaryPath.getTotalTimeSeconds() / 60).append(" min).");
        if (requireAccessible) {
            rationale.append(" Verified fully accessible via ramps and elevators.");
        }
        if (!obstacleDescriptions.isEmpty()) {
            rationale.append(" Successfully bypassed ").append(obstacleDescriptions.size()).append(" active obstacle(s).");
        }
        response.setRecommendationRationale(rationale.toString());

        // Compute Alternative Routes if available (from other gates or second best)
        List<RouteCalculationResponse> alternatives = new ArrayList<>();
        for (int i = 1; i < Math.min(evaluatedGates.size(), 3); i++) {
            GateSelectionService.EvaluatedGate alt = evaluatedGates.get(i);
            RouteCalculationResponse altResp = new RouteCalculationResponse();
            altResp.setRouteFound(true);
            altResp.setRecommendedGate(NodeDto.fromEntity(alt.getGateNode()));
            altResp.setTotalDistanceMeters(alt.getPathResult().getTotalDistance());
            altResp.setEstimatedTimeSeconds(alt.getPathResult().getTotalTimeSeconds());
            altResp.setPathNodes(alt.getPathResult().getNodes().stream().map(NodeDto::fromEntity).collect(Collectors.toList()));
            altResp.setPathEdges(alt.getPathResult().getEdges().stream().map(EdgeDto::fromEntity).collect(Collectors.toList()));
            altResp.setInstructions(generateInstructions(alt.getPathResult().getNodes(), alt.getPathResult().getEdges()));
            altResp.setRecommendationRationale("Alternative entrance via " + alt.getGateNode().getName() + " (" +
                    String.format("%.0f", alt.getPathResult().getTotalDistance()) + "m).");
            alternatives.add(altResp);
        }
        response.setAlternativeRoutes(alternatives);

        return response;
    }

    private List<RouteInstructionDto> generateInstructions(List<LocationNode> nodes, List<LocationEdge> edges) {
        List<RouteInstructionDto> instructions = new ArrayList<>();
        if (nodes.isEmpty()) return instructions;

        // Step 1: Entry gate
        LocationNode start = nodes.get(0);
        instructions.add(new RouteInstructionDto(
                1,
                "Enter through " + start.getName() + " and proceed into internal access network",
                start.getName(),
                start.getNodeType().name(),
                0.0,
                10,
                "GATE"
        ));

        int stepNum = 2;
        for (int i = 0; i < edges.size(); i++) {
            LocationEdge edge = edges.get(i);
            LocationNode targetNode = (i + 1 < nodes.size()) ? nodes.get(i + 1) : edge.getDestinationNode();

            String icon = "CORRIDOR";
            String action = "Proceed along pathway towards " + targetNode.getName();

            NodeType type = targetNode.getNodeType();
            if (type == NodeType.INTERNAL_ROAD) {
                icon = "ROAD";
                action = "Follow internal roadway towards " + targetNode.getName();
            } else if (type == NodeType.BUILDING || type == NodeType.TOWER || type == NodeType.BLOCK) {
                icon = "BUILDING";
                action = "Approach entrance and enter " + targetNode.getName();
            } else if (type == NodeType.LIFT) {
                icon = "LIFT";
                action = "Take " + targetNode.getName() + " to level " + (targetNode.getLevelNumber() != null ? targetNode.getLevelNumber() : 0);
            } else if (type == NodeType.STAIRCASE) {
                icon = "STAIRS";
                action = "Use " + targetNode.getName() + " to ascend/descend to level " + (targetNode.getLevelNumber() != null ? targetNode.getLevelNumber() : 0);
            } else if (type == NodeType.FLOOR || type == NodeType.ZONE) {
                icon = "CORRIDOR";
                action = "Step out onto " + targetNode.getName();
            } else if (type == NodeType.AISLE || type == NodeType.CORRIDOR) {
                icon = "CORRIDOR";
                action = "Walk down " + targetNode.getName();
            } else if (i == edges.size() - 1) {
                icon = "DESTINATION";
                action = "Arrive at exact destination: " + targetNode.getName();
            }

            double dist = edge.getDistanceMeters() != null ? edge.getDistanceMeters() : 10.0;
            int time = edge.getEstimatedTimeSeconds() != null ? edge.getEstimatedTimeSeconds() : 15;

            instructions.add(new RouteInstructionDto(
                    stepNum++,
                    action + " (" + String.format("%.0f", dist) + "m)",
                    targetNode.getName(),
                    targetNode.getNodeType().name(),
                    dist,
                    time,
                    icon
            ));
        }

        return instructions;
    }
}
