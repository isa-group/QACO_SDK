package com.example.qaco.domain.binding;

import com.example.qaco.domain.cws.Task;
import com.example.qaco.domain.cws.CandidateService;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maps a single Task to a chosen CandidateService.
 */
@Data
@NoArgsConstructor
public class BindingMapping {
    private Task task;
    private CandidateService candidateService;
}
