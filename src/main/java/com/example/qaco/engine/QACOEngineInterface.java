package com.example.qaco.engine;

import com.example.qaco.domain.QACOProblem;
import com.example.qaco.domain.binding.Binding;
import com.example.qaco.domain.binding.BindingSpace;
import com.example.qaco.domain.cws.CompositeWebService;

import java.util.List;
import java.util.Optional;

/**
 * A generic interface for solving QACO problems and computing binding spaces.
 */
public interface QACOEngineInterface {

    /**
     * Takes a QACOProblem, validates it, computes a solution, and returns an optimal Binding or a list of them.
     * 
     * @param problem the QACOProblem to solve
     * @param extraConfig an optional extra configuration object that it may be needed for a specific solver/engine.
     * @return an optional containing a list of optimal bindings, or empty if no solution is found
     * @throws IllegalArgumentException if the problem is invalid
     */
    Optional<List<Binding>> solve(QACOProblem problem, Optional<Object> extraConfig) throws IllegalArgumentException;

    /**
     * Returns the binding space for a given CompositeWebService.
     * 
     * @param cws the CompositeWebService to get the binding space for
     * @param extraConfig an optional extra configuration object that it may be needed for a specific solver/engine.
     * @return an optional containing the binding space, or empty if no binding space is found
     * @throws IllegalArgumentException if the CompositeWebService is invalid
     */
    Optional<BindingSpace> bindingSpace(CompositeWebService cws, Optional<Object> extraConfig) throws IllegalArgumentException;
}
