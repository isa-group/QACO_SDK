package com.example.qaco.domain.cws.graph;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.example.qaco.domain.cws.graph.probability.Probability;

/**
 * Represents the graph structure for the QACO problem (nodes, edges, etc.).
 */
@Data
@NoArgsConstructor
public class Graph {
    private List<GraphNode> nodes;
    private List<GraphEdge> edges;
    private List<Probability> probabilities;
}
