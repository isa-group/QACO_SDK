package com.example.qaco.domain;

import com.example.qaco.domain.cws.CompositeWebService;
import com.example.qaco.domain.problem.Problem;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the overall QACO Problem definition, tying together:
 * - Problem (constraints, optimization)
 * - Graph structure
 * - Probability model
 * - Composite Web Service (tasks, features, candidate services)
 * - (Optional) a BindingSpace if pre-defined
 */
@Data
@NoArgsConstructor
public class QACOProblem {
    private CompositeWebService compositeWebService;
    private Problem problem;
}
