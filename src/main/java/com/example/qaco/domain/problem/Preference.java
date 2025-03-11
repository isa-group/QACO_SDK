package com.example.qaco.domain.problem;

import com.example.qaco.domain.cws.Feature;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Preference {
    private Feature feature;
    private Double weight;
}
