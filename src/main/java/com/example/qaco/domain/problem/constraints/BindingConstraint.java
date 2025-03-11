package com.example.qaco.domain.problem.constraints;

import java.util.List;

import com.example.qaco.domain.cws.Task;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Represents a constraint that applies globally across the entire solution space.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class BindingConstraint extends Constraint {
    private List<Task> providers;
    private Operator operator;
}
