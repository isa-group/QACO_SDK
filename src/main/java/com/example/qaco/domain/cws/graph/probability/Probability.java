package com.example.qaco.domain.cws.graph.probability;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the probability distribution used by QACO.
 */
@Data
@NoArgsConstructor
public class Probability {
    private List<ProbabilityNode> nodes;
}
