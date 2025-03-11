package com.example.qaco.domain.problem;

import com.example.qaco.domain.problem.constraints.Constraint;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the "Problem" entity in the domain.
 */
@Data
@NoArgsConstructor
public class Problem {
    private String name;
    private String description;
    private Optimization optimization;
    private List<Constraint> constraints;
}
