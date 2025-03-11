package com.example.qaco.domain.cws.graph;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a directed or undirected edge between two nodes in the graph.
 */
@Data
@NoArgsConstructor
public class GraphEdge {
    private GraphNode source;
    private GraphNode target;
    private String label;
}
