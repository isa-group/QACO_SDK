package com.example.qaco.domain.cws;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FeatureValue {
    private CandidateService service;
    private Double value;
}
