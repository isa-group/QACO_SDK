package com.example.qaco.domain.problem.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a constraint that applies to a local part of the solution (e.g., a single task or feature).
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class LocalConstraint extends Constraint {
    private FeatureConstraint inputFeature;
    private Operator operator;
    private Double value;
    private FeatureConstraint outputFeature;
}
