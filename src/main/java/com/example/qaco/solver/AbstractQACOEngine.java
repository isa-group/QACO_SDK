package com.example.qaco.solver;

import com.example.qaco.domain.QACOProblem;
import com.example.qaco.domain.binding.Binding;
import com.example.qaco.domain.binding.BindingMapping;
import com.example.qaco.domain.binding.BindingSpace;
import com.example.qaco.domain.cws.*;
import com.example.qaco.domain.cws.graph.Graph;
import com.example.qaco.domain.cws.graph.GraphNodeType;
import com.example.qaco.domain.problem.Problem;
import com.example.qaco.domain.problem.Optimization;
import com.example.qaco.domain.problem.Preference;
import com.example.qaco.domain.problem.constraints.*;

import java.util.*;
import java.util.Optional;

/**
 * An abstract base class for QACOEngineInterface implementations.
 * It provides standard validation for input (QACOProblem, CompositeWebService)
 * and output (Binding, BindingSpace).
 */
public abstract class AbstractQACOEngine implements QACOEngineInterface {

    @Override
    public Optional<List<Binding>> solve(QACOProblem problem, Optional<Object> extraConfig) throws IllegalArgumentException {
        validateInput(problem);

        Optional<List<Binding>> result = doSolve(problem, extraConfig);

        // Validate each Binding in the returned list
        result.ifPresent(bindings -> bindings.forEach(this::validateOutput));
        
        return result;
    }

    /**
     * Perform the actual solving. Must be implemented by concrete solvers.
     */
    protected abstract Optional<List<Binding>> doSolve(QACOProblem problem, Optional<Object> extraConfig);

    /**
     * Validate the input problem to ensure it's well-formed.
     */
    protected void validateInput(QACOProblem problem) {
        if (problem == null) {
            throw new IllegalArgumentException("QACOProblem cannot be null.");
        }
        Problem domainProblem = problem.getProblem();
        if (domainProblem == null) {
            throw new IllegalArgumentException("Problem definition is missing in QACOProblem.");
        }
        CompositeWebService cws = problem.getCompositeWebService();
        if (cws == null) {
            throw new IllegalArgumentException("CompositeWebService is missing in QACOProblem.");
        }

        // 1. Validate the CWS structure
        validateInput(cws);

        // 2. Validate the domain problem (constraints, optimization, etc.) with respect to the CWS
        validateProblemDefinition(domainProblem, cws);
    }

    /**
     * Validate the input CompositeWebService to ensure it's well-formed.
     */
    protected void validateInput(CompositeWebService cws) {
        if (cws.getTasks() == null || cws.getTasks().isEmpty()) {
            throw new IllegalArgumentException("CompositeWebService must have at least one Task.");
        }
        if (cws.getCandidateServices() == null || cws.getCandidateServices().isEmpty()) {
            throw new IllegalArgumentException("CompositeWebService must have at least one CandidateService.");
        }

        // Validate each Task
        for (Task task : cws.getTasks()) {
            validateTask(task);
        }
        // Validate each CandidateService
        for (CandidateService cs : cws.getCandidateServices()) {
            validateCandidateService(cs);
        }
        // Validate each Feature
        if (cws.getFeatures() != null) {
            for (Feature feature : cws.getFeatures()) {
                validateFeature(feature);
            }
        }
        // Validate the graph has at least one START and one END node
        if (cws.getGraph() != null) {
            validateGraph(cws.getGraph());
        }
    }

    private void validateTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null.");
        }
        if (task.getName() == null || task.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Task name cannot be null or empty.");
        }
    }

    private void validateCandidateService(CandidateService cs) {
        if (cs == null) {
            throw new IllegalArgumentException("CandidateService cannot be null.");
        }
        if (cs.getName() == null || cs.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("CandidateService name cannot be null or empty.");
        }
        // Additional checks for provider, tasks, etc. can go here.
    }

    private void validateFeature(Feature feature) {
        if (feature.getName() == null || feature.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Feature name cannot be null or empty.");
        }
        // Additional validations on FeatureValue if needed
    }

    private void validateGraph(Graph graph) {
        // Must contain at least one START and one END node
        if (graph.getNodes() == null || graph.getNodes().isEmpty()) {
            throw new IllegalArgumentException("Graph must contain at least one node.");
        }
        boolean hasStart = graph.getNodes().stream().anyMatch(n -> n.getType() == GraphNodeType.START);
        boolean hasEnd = graph.getNodes().stream().anyMatch(n -> n.getType() == GraphNodeType.END);

        if (!hasStart) {
            throw new IllegalArgumentException("Graph must contain at least one START node.");
        }
        if (!hasEnd) {
            throw new IllegalArgumentException("Graph must contain at least one END node.");
        }
    }

    /**
     * Validate the domain Problem object (constraints, optimization, etc.) 
     * in the context of the given CompositeWebService.
     */
    private void validateProblemDefinition(Problem domainProblem, CompositeWebService cws) {
        // Collect all CWS tasks and features in sets for quick lookup
        Set<Task> cwsTasks = new HashSet<>(Optional.ofNullable(cws.getTasks()).orElse(Collections.emptyList()));
        Set<Feature> cwsFeatures = new HashSet<>(Optional.ofNullable(cws.getFeatures()).orElse(Collections.emptyList()));

        // 1. Check Optimization
        if (domainProblem.getOptimization() != null) {
            validateOptimization(domainProblem.getOptimization(), cwsFeatures);
        }

        // 2. Check Constraints
        if (domainProblem.getConstraints() != null) {
            for (Constraint constraint : domainProblem.getConstraints()) {
                validateConstraint(constraint, cwsTasks, cwsFeatures, domainProblem.getOptimization());
            }
        }
    }

    /**
     * Validate the optimization: 
     * - All features in preferences must exist in the CWS.
     */
    private void validateOptimization(Optimization optimization, Set<Feature> cwsFeatures) {
        // Collect features used in optimization
        // For each Preference, we have preference.getFeature()
        if (optimization.getPreferences() != null) {
            for (Preference pref : optimization.getPreferences()) {
                Feature f = pref.getFeature();
                if (f != null && !cwsFeatures.contains(f)) {
                    throw new IllegalArgumentException(
                            "Optimization references a Feature not present in the CWS: " + f.getName());
                }
            }
        }
        // aggregatorOperation can also reference features, so check them if needed
        if (optimization.getAggregateDomains() != null) {
            optimization.getAggregateDomains().forEach(ad -> {
                if (ad.getAggregatorOperation() != null) {
                    ad.getAggregatorOperation().forEach(op -> {
                        if (op.getFeatures() != null) {
                            for (Feature f : op.getFeatures()) {
                                if (!cwsFeatures.contains(f)) {
                                    throw new IllegalArgumentException(
                                            "AggregatorOperation references a Feature not present in the CWS: "
                                                    + f.getName());
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    /**
     * Validate each constraint with respect to the tasks and features in the CWS,
     * and also check any logic that ties constraints to optimization features.
     */
    private void validateConstraint(
            Constraint constraint,
            Set<Task> cwsTasks,
            Set<Feature> cwsFeatures,
            Optimization optimization
    ) {
        if (constraint instanceof GlobalConstraint) {
            validateGlobalConstraint((GlobalConstraint) constraint, cwsFeatures, optimization);
        } else if (constraint instanceof LocalConstraint) {
            validateLocalConstraint((LocalConstraint) constraint, cwsTasks, cwsFeatures);
        } else if (constraint instanceof ComposeConstraint) {
            validateComposeConstraint((ComposeConstraint) constraint, cwsTasks, cwsFeatures, optimization);
        } else if (constraint instanceof ConditionalConstraint) {
            validateConditionalConstraint((ConditionalConstraint) constraint, cwsTasks, cwsFeatures, optimization);
        } else if (constraint instanceof BindingConstraint) {
            validateBindingConstraint((BindingConstraint) constraint, cwsTasks);
        }
        // If you add new constraint types, handle them here.
    }

    private void validateGlobalConstraint(GlobalConstraint gc, Set<Feature> cwsFeatures, Optimization optimization) {
        // Check that the feature is in the CWS
        if (gc.getInputFeature() != null && !cwsFeatures.contains(gc.getInputFeature())) {
            throw new IllegalArgumentException("GlobalConstraint references a feature not in the CWS: "
                    + gc.getInputFeature().getName());
        }
        // Check that global constraint's feature is also in the optimization features (requirement)
        if (optimization != null && optimization.getPreferences() != null) {
            boolean found = optimization.getPreferences().stream()
                    .anyMatch(pref -> pref.getFeature() == gc.getInputFeature());
            if (!found) {
                throw new IllegalArgumentException("GlobalConstraint feature must be part of Optimization's features: "
                        + gc.getInputFeature().getName());
            }
        }
    }

    private void validateLocalConstraint(LocalConstraint lc, Set<Task> cwsTasks, Set<Feature> cwsFeatures) {
        // Check the input feature tasks are in the CWS
        if (lc.getInputFeature() != null) {
            FeatureConstraint fc = lc.getInputFeature();
            validateFeatureConstraint(fc, cwsTasks, cwsFeatures);
        }
        // Check that if we have an outputFeature, then 'value' must be null
        if (lc.getOutputFeature() != null) {
            // outputFeature must be valid
            validateFeatureConstraint(lc.getOutputFeature(), cwsTasks, cwsFeatures);
            // 'value' must be null
            if (lc.getValue() != null) {
                throw new IllegalArgumentException(
                        "LocalConstraint cannot have both outputFeature and a numeric value.");
            }
        }
    }

    private void validateComposeConstraint(
            ComposeConstraint cc,
            Set<Task> cwsTasks,
            Set<Feature> cwsFeatures,
            Optimization optimization
    ) {
        // ComposeConstraint can hold multiple sub-conditions
        if (cc.getConditions() != null) {
            for (Constraint subConstraint : cc.getConditions()) {
                validateConstraint(subConstraint, cwsTasks, cwsFeatures, optimization);
            }
        }
    }

    private void validateConditionalConstraint(
            ConditionalConstraint cond,
            Set<Task> cwsTasks,
            Set<Feature> cwsFeatures,
            Optimization optimization
    ) {
        // Condition, then
        if (cond.getCondition() != null) {
            validateConstraint(cond.getCondition(), cwsTasks, cwsFeatures, optimization);
        }
        if (cond.getThen() != null) {
            validateConstraint(cond.getThen(), cwsTasks, cwsFeatures, optimization);
        }
    }

    private void validateBindingConstraint(BindingConstraint bc, Set<Task> cwsTasks) {
        // Check that the tasks in the constraint are in the CWS
        if (bc.getProviders() != null) {
            for (Task t : bc.getProviders()) {
                if (!cwsTasks.contains(t)) {
                    throw new IllegalArgumentException("BindingConstraint references a Task not in the CWS: "
                            + t.getName());
                }
            }
        }
    }

    private void validateFeatureConstraint(FeatureConstraint fc, Set<Task> cwsTasks, Set<Feature> cwsFeatures) {
        // Check feature is in the CWS
        if (fc.getFeature() != null && !cwsFeatures.contains(fc.getFeature())) {
            throw new IllegalArgumentException("FeatureConstraint references a Feature not in the CWS: "
                    + fc.getFeature().getName());
        }
        // Check tasks are in the CWS
        if (fc.getTasks() != null) {
            for (Task t : fc.getTasks()) {
                if (!cwsTasks.contains(t)) {
                    throw new IllegalArgumentException("FeatureConstraint references a Task not in the CWS: "
                            + t.getName());
                }
            }
        }
    }

    /**
     * Validate the resulting Binding (output).
     */
    protected void validateOutput(Binding binding) {
        if (binding.getBindingMappings() == null || binding.getBindingMappings().isEmpty()) {
            throw new IllegalStateException("Solution binding must contain at least one BindingMapping.");
        }
        // Validate each BindingMapping
        for (BindingMapping mapping : binding.getBindingMappings()) {
            if (mapping.getTask() == null) {
                throw new IllegalStateException("BindingMapping must have a non-null Task.");
            }
            if (mapping.getCandidateService() == null) {
                throw new IllegalStateException("BindingMapping must have a non-null CandidateService.");
            }
        }
    }

    @Override
    public Optional<BindingSpace> bindingSpace(CompositeWebService cws, Optional<Object> extraConfig) throws IllegalArgumentException {
        validateInput(cws);

        Optional<BindingSpace> result = getBindingSpace(cws, extraConfig);

        // Validate each Binding in the returned BindingSpace
        result.ifPresent(bindingSpace -> {
            if (bindingSpace.getBindings() == null || bindingSpace.getBindings().isEmpty()) {
                throw new IllegalStateException("BindingSpace must contain at least one Binding.");
            }
            bindingSpace.getBindings().forEach(this::validateOutput);
        });

        return result;
    }

    /**
     * Get the binding space for the given CompositeWebService.
     */
    protected abstract Optional<BindingSpace> getBindingSpace(CompositeWebService cws, Optional<Object> extraConfig);
}
