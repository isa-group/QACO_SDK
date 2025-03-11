package com.example.qaco.domain.problem.constraints;

import java.util.List;

import com.example.qaco.domain.cws.Feature;
import com.example.qaco.domain.cws.Task;
import com.example.qaco.domain.problem.AggregatorOperation;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class FeatureConstraint {
    private Feature feature;
    private List<Task> tasks;
    private AggregatorOperation aggregator;
}
