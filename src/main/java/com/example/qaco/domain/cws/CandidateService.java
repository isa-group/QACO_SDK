package com.example.qaco.domain.cws;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a candidate service that can fulfill a specific task.
 */
@Data
@NoArgsConstructor
public class CandidateService {
    private String name;
    private String description;
    private String provider;

    // The candidate service can fulfill multiple tasks
    private List<Task> tasks;
}
