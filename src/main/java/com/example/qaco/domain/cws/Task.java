package com.example.qaco.domain.cws;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single task in the composite web service.
 */
@Data
@NoArgsConstructor
public class Task {
    private String name;
    private String description;
}
