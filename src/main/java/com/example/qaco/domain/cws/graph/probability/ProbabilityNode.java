package com.example.qaco.domain.cws.graph.probability;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a probability node (e.g., associated with a graph node).
 */
@Data
@NoArgsConstructor
public class ProbabilityNode {
    private List<ProbabilityEdge> edges;
}
