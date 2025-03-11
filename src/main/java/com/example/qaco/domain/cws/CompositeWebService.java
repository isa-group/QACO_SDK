package com.example.qaco.domain.cws;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.example.qaco.domain.cws.graph.Graph;

/**
 * Represents a composite web service that consists of multiple tasks, candidate services, and features. It also has a graph structure that represents it.
 */
@Data
@NoArgsConstructor
public class CompositeWebService {
    private String name;
    private String description;

    private List<Task> tasks;
    private List<CandidateService> candidateServices;
    private List<Feature> features;
    private Graph graph;
}
