package com.example.qaco.domain.cws.graph;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single node in the graph.
 */
@Data
@NoArgsConstructor
public class GraphNode {
    private String label;
    private GraphNodeType type;
}
