package com.example.qaco.domain.problem.constraints;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a constraint that applies when composing services/features.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ComposeConstraint extends Constraint {
    private ComposeConstraintType type;
    private List<Constraint> conditions;
}
