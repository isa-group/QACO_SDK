package com.example.qaco.domain.problem;

import java.util.List;

import com.example.qaco.domain.cws.Feature;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AggregatorOperation {
    private List<Feature> features;
    private String operation;
}
