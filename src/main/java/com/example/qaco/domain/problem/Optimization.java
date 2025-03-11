package com.example.qaco.domain.problem;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Encapsulates how we want to optimize the problem:
 * - preference (maximize or minimize)
 * - aggregatorOperation (sum, average, etc.)
 */
@Data
@NoArgsConstructor
public class Optimization {
    private List<Preference> preferences;
    private List<AggregateDomain> aggregateDomains;
}
