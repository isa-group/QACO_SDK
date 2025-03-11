package com.example.qaco.domain.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a specific binding configuration (i.e., how tasks map to candidate services).
 */
@Data
@NoArgsConstructor
public class Binding {
    private List<BindingMapping> bindingMappings;
}
