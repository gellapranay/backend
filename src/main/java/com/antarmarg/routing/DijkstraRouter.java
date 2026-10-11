package com.antarmarg.routing;

import com.antarmarg.entity.LocationEdge;
import com.antarmarg.entity.LocationNode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DijkstraRouter {

    public static class PathResult {
        private final List<LocationNode> nodes;
        private final List<LocationEdge> edges;
        private final double totalDistance;
        private final int totalTimeSeconds;

        public PathResult(List<LocationNode> nodes, List<LocationEdge> edges, double totalDistance, int totalTimeSeconds) {
            this.nodes = nodes;
            this.edges = edges;
            this.totalDistance = totalDistance;
            this.totalTimeSeconds = totalTimeSeconds;
        }

        public List<LocationNode> getNodes() {
            return nodes;
        }

        public List<LocationEdge> getEdges() {
            return edges;
        }

        public double getTotalDistance() {
            return totalDistance;
        }

        public int getTotalTimeSeconds() {
            return totalTimeSeconds;
        }
    }

    private static class NodeRecord implements Comparable<NodeRecord> {
        final Long nodeId;
        final double cost;

        NodeRecord(Long nodeId, double cost) {
            this.nodeId = nodeId;
            this.cost = cost;
        }

        @Override
        public int compareTo(NodeRecord o) {
            return Double.compare(this.cost, o.cost);
        }
    }

    private static class EdgeNeighbor {
        final LocationNode targetNode;
        final LocationEdge edge;
        final double weight;

        EdgeNeighbor(LocationNode targetNode, LocationEdge edge, double weight) {
            this.targetNode = targetNode;
            this.edge = edge;
            this.weight = weight;
        }
    }

    /**
     * Compute shortest path from startNode to endNode using Dijkstra algorithm.
     * Excludes blocked edges, inactive edges, inactive nodes, and blocked nodes.
     */
    public Optional<PathResult> findShortestPath(
            LocationNode startNode,
            LocationNode endNode,
            List<LocationEdge> allEdges,
            Set<Long> blockedEdgeIds,
            Set<Long> blockedNodeIds,
            boolean requireAccessible,
            boolean isVehicle,
            Set<Long> edgeIdsToPenalize) {

        if (startNode == null || endNode == null) {
            return Optional.empty();
        }

        if (startNode.getId().equals(endNode.getId())) {
            return Optional.of(new PathResult(List.of(startNode), List.of(), 0.0, 0));
        }

        if (blockedNodeIds.contains(startNode.getId()) || blockedNodeIds.contains(endNode.getId())) {
            return Optional.empty();
        }

        // Build adjacency graph
        Map<Long, List<EdgeNeighbor>> graph = new HashMap<>();

        for (LocationEdge edge : allEdges) {
            if (!Boolean.TRUE.equals(edge.getIsActive())) continue;
            if ("BLOCKED".equalsIgnoreCase(edge.getStatus())) continue;
            if (blockedEdgeIds.contains(edge.getId())) continue;

            LocationNode src = edge.getSourceNode();
            LocationNode dst = edge.getDestinationNode();

            if (src == null || dst == null) continue;
            if (!Boolean.TRUE.equals(src.getIsActive()) || !Boolean.TRUE.equals(dst.getIsActive())) continue;
            if (blockedNodeIds.contains(src.getId()) || blockedNodeIds.contains(dst.getId())) continue;

            // Accessibility filter
            if (requireAccessible && !Boolean.TRUE.equals(edge.getIsAccessible())) {
                continue;
            }

            double baseWeight = edge.getDistanceMeters();
            if (edgeIdsToPenalize != null && edgeIdsToPenalize.contains(edge.getId())) {
                baseWeight += 1000.0; // heavy penalty for finding alternative routes
            }

            graph.computeIfAbsent(src.getId(), k -> new ArrayList<>())
                    .add(new EdgeNeighbor(dst, edge, baseWeight));

            if (Boolean.TRUE.equals(edge.getIsBidirectional())) {
                graph.computeIfAbsent(dst.getId(), k -> new ArrayList<>())
                        .add(new EdgeNeighbor(src, edge, baseWeight));
            }
        }

        // Dijkstra algorithm with PriorityQueue
        PriorityQueue<NodeRecord> pq = new PriorityQueue<>();
        Map<Long, Double> distances = new HashMap<>();
        Map<Long, Long> previousNodes = new HashMap<>();
        Map<Long, LocationEdge> previousEdges = new HashMap<>();
        Map<Long, LocationNode> nodeDirectory = new HashMap<>();

        nodeDirectory.put(startNode.getId(), startNode);
        nodeDirectory.put(endNode.getId(), endNode);

        for (LocationEdge e : allEdges) {
            if (e.getSourceNode() != null) nodeDirectory.put(e.getSourceNode().getId(), e.getSourceNode());
            if (e.getDestinationNode() != null) nodeDirectory.put(e.getDestinationNode().getId(), e.getDestinationNode());
        }

        distances.put(startNode.getId(), 0.0);
        pq.add(new NodeRecord(startNode.getId(), 0.0));

        while (!pq.isEmpty()) {
            NodeRecord current = pq.poll();
            Long currentId = current.nodeId;

            if (current.cost > distances.getOrDefault(currentId, Double.MAX_VALUE)) {
                continue;
            }

            if (currentId.equals(endNode.getId())) {
                break; // Reached target
            }

            List<EdgeNeighbor> neighbors = graph.getOrDefault(currentId, Collections.emptyList());
            for (EdgeNeighbor neighbor : neighbors) {
                Long nextId = neighbor.targetNode.getId();
                double newDist = current.cost + neighbor.weight;

                if (newDist < distances.getOrDefault(nextId, Double.MAX_VALUE)) {
                    distances.put(nextId, newDist);
                    previousNodes.put(nextId, currentId);
                    previousEdges.put(nextId, neighbor.edge);
                    pq.add(new NodeRecord(nextId, newDist));
                }
            }
        }

        if (!distances.containsKey(endNode.getId())) {
            return Optional.empty(); // No reachable path
        }

        // Reconstruct path
        List<LocationNode> pathNodes = new ArrayList<>();
        List<LocationEdge> pathEdges = new ArrayList<>();
        Long curr = endNode.getId();

        double totalDistance = 0.0;
        int totalTime = 0;

        while (curr != null) {
            LocationNode n = nodeDirectory.get(curr);
            if (n != null) {
                pathNodes.add(0, n);
            }
            LocationEdge e = previousEdges.get(curr);
            if (e != null) {
                pathEdges.add(0, e);
                totalDistance += (e.getDistanceMeters() != null ? e.getDistanceMeters() : 10.0);
                totalTime += (e.getEstimatedTimeSeconds() != null ? e.getEstimatedTimeSeconds() : 15);
            }
            curr = previousNodes.get(curr);
        }

        return Optional.of(new PathResult(pathNodes, pathEdges, totalDistance, totalTime));
    }
}
