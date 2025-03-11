package com.example.qaco.domain.binding;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the entire space of possible bindings.
 */
@Data
@NoArgsConstructor
public class BindingSpace {
    private List<Binding> bindings;
}
