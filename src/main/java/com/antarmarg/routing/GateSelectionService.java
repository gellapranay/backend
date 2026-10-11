package com.antarmarg.routing;

import com.antarmarg.entity.LocationEdge;
import com.antarmarg.entity.LocationNode;
import com.antarmarg.entity.NodeType;
import com.antarmarg.repository.LocationNodeRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GateSelectionService {

    private final LocationNodeRepository nodeRepository;
    private final DijkstraRouter dijkstraRouter;

    public GateSelectionService(LocationNodeRepository nodeRepository, DijkstraRouter dijkstraRouter) {
        this.nodeRepository = nodeRepository;
        this.dijkstraRouter = dijkstraRouter;
    }

    public static class EvaluatedGate {
        private final LocationNode gateNode;
        private final DijkstraRouter.PathResult pathResult;
        private final double score;

        public EvaluatedGate(LocationNode gateNode, DijkstraRouter.PathResult pathResult, double score) {
            this.gateNode = gateNode;
            this.pathResult = pathResult;
            this.score = score;
        }

        public LocationNode getGateNode() {
            return gateNode;
        }

        public DijkstraRouter.PathResult getPathResult() {
            return pathResult;
        }

        public double getScore() {
            return score;
        }
    }

    /**
     * Finds and scores all permitted, reachable gates for the target destination.
     */
    public List<EvaluatedGate> evaluateGates(
            Long facilityId,
            LocationNode destinationNode,
            List<LocationEdge> allEdges,
            Set<Long> blockedEdgeIds,
            Set<Long> blockedNodeIds,
            boolean requireAccessible,
            boolean isVehicle,
            Long preferredGateId) {

        List<LocationNode> gates = nodeRepository.findByFacilityIdAndNodeType(facilityId, NodeType.ENTRY_GATE);
        if (gates.isEmpty()) {
            // Fallback: check nodes with parent null or named gate
            gates = nodeRepository.findByFacilityIdAndParentNodeIsNull(facilityId);
        }

        List<EvaluatedGate> candidateGates = new ArrayList<>();

        for (LocationNode gate : gates) {
            if (!Boolean.TRUE.equals(gate.getIsActive())) continue;
            if (blockedNodeIds.contains(gate.getId())) continue;

            Optional<DijkstraRouter.PathResult> pathOpt = dijkstraRouter.findShortestPath(
                    gate,
                    destinationNode,
                    allEdges,
                    blockedEdgeIds,
                    blockedNodeIds,
                    requireAccessible,
                    isVehicle,
                    null
            );

            if (pathOpt.isPresent()) {
                DijkstraRouter.PathResult path = pathOpt.get();
                // Transparent Scoring Formula:
                // baseScore = totalDistance + (timeSeconds * 1.5)
                // If preferredGateId matches, grant bonus discount
                double score = path.getTotalDistance() + (path.getTotalTimeSeconds() * 1.5);
                if (preferredGateId != null && preferredGateId.equals(gate.getId())) {
                    score -= 500.0; // Preference bonus
                }
                candidateGates.add(new EvaluatedGate(gate, path, score));
            }
        }

        // Sort by lowest score (most optimal)
        candidateGates.sort(Comparator.comparingDouble(EvaluatedGate::getScore));
        return candidateGates;
    }
}
