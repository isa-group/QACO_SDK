package com.example.qaco.domain.cws;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a feature (e.g., QoS parameter) for a task or service.
 */
@Data
@NoArgsConstructor
public class Feature {
    private String name;
    private String description;

    // The value of the feature for a specific service
    private List<FeatureValue> values;
}
