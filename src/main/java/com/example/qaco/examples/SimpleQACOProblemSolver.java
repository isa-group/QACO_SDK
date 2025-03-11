package com.example.qaco.examples;

import com.example.qaco.domain.QACOProblem;
import com.example.qaco.domain.binding.Binding;
import com.example.qaco.domain.binding.BindingMapping;
import com.example.qaco.domain.binding.BindingSpace;
import com.example.qaco.domain.cws.CandidateService;
import com.example.qaco.domain.cws.CompositeWebService;
import com.example.qaco.domain.cws.Task;
import com.example.qaco.solver.AbstractQACOProblemSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SimpleQACOProblemSolver extends AbstractQACOProblemSolver {
    private final Random random = new Random();

    @Override
    protected Optional<List<Binding>> doSolve(QACOProblem problem) {
        // Construct a trivial solution: pick the first candidate for each task
        Binding binding = new Binding();
        binding.setBindingMappings(new ArrayList<>());

        // Iterate over tasks
        if (problem.getCompositeWebService().getTasks() != null && problem.getCompositeWebService().getCandidateServices() != null) {
            List<CandidateService> candidateServices = problem.getCompositeWebService().getCandidateServices();
            for (Task task : problem.getCompositeWebService().getTasks()) {
                if (!candidateServices.isEmpty()) {
                    CandidateService chosen = candidateServices.get(random.nextInt(candidateServices.size()));
                    BindingMapping mapping = new BindingMapping();
                    mapping.setTask(task);
                    mapping.setCandidateService(chosen);
                    binding.getBindingMappings().add(mapping);
                }
            }
        }

        List<Binding> bindings = new ArrayList<>();
        bindings.add(binding);
        return Optional.of(bindings);
    }

    @Override
    protected Optional<BindingSpace> getBindingSpace(CompositeWebService cws) {
        BindingSpace bindingSpace = new BindingSpace();
        
        // Construct a trivial binding space: all candidate services for each task
        bindingSpace.setBindings(new ArrayList<>());
        if (cws.getTasks() != null && cws.getCandidateServices() != null) {
            for (Task task : cws.getTasks()) {
                Binding binding = new Binding();
                binding.setBindingMappings(new ArrayList<>());
                for (CandidateService candidateService : cws.getCandidateServices()) {
                    BindingMapping mapping = new BindingMapping();
                    mapping.setTask(task);
                    mapping.setCandidateService(candidateService);
                    binding.getBindingMappings().add(mapping);
                }
                bindingSpace.getBindings().add(binding);
            }
        }

        return Optional.of(bindingSpace);
    }
}
