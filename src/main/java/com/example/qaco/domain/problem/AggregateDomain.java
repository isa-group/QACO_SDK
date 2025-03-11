package com.example.qaco.domain.problem;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AggregateDomain {
    private List<AggregatorOperation> aggregatorOperation;
    private AggregateDomainType aggregateDomainType;
}