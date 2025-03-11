package com.example.qaco.domain.problem.constraints;

import com.example.qaco.domain.cws.Feature;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a constraint that applies globally across the entire solution space.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GlobalConstraint extends Constraint {
    private Feature inputFeature;
    private Operator operator;
    private Double value;
}
