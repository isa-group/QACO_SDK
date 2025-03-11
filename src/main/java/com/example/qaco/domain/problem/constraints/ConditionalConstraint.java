package com.example.qaco.domain.problem.constraints;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a constraint that applies conditionally based on certain criteria.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ConditionalConstraint extends Constraint {
    private Constraint condition;
    private Constraint then;
}
