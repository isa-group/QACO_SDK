# QACO SDK

**QoS-Aware COmposition (QACO)** is a process that helps you define, validate, and solve web service composition problems involving tasks, candidate services, constraints, QoS features and optimization preferences. This SDK provides:

- A **rich domain model** for QACO problems (Tasks, Features, Constraints, etc.).  
- A **validation** layer to ensure inputs (QACOProblem, CompositeWebService) and outputs (Binding, BindingSpace) are consistent.  
- An **extendable engine interface** to implement your own QACO algorithms and solvers.

---

## Table of Contents

1. [Features](#features)  
2. [Architecture & Domain Model](#architecture--domain-model)  
3. [Installation](#installation)  
4. [Usage](#usage)  
   - [Defining a QACOProblem](#defining-a-qacoproblem)  
   - [Implementing an Engine](#implementing-an-engine)  
   - [Validations](#validations)  
5. [Testing in Another Project](#testing-in-another-project)  
6. [OpenAPI Specification (Optional)](#openapi-specification-optional)  
7. [License](#license)

---

## Features

1. **Domain Model**:  
   - `QACOProblem` ties together a `CompositeWebService` (CWS) and a `Problem` definition.  
   - `CompositeWebService` includes tasks, candidate services, features, and a graph structure.  
   - `Problem` includes constraints (Global, Local, Compose, Conditional, Binding) and an optimization strategy.

2. **Validation**:  
   - Ensures that tasks, features, and constraints reference valid entities.  
   - Checks graph nodes for at least one `START` and one `END`.  
   - Verifies that constraints’ features exist in the CWS and that local constraints do not mix `value` and `outputFeature` incorrectly.

3. **Engines**:  
   - `QACOEngineInterface` is an interface defining two main methods: `solve(QACOProblem)` and `bindingSpace(CompositeWebService)`.  
   - `AbstractQACOEngine` provides a **default validation** mechanism for input and output.  
   - You can extend it with custom algorithms (e.g., metaheuristics, exact solvers).

4. **Bindings**:  
   - The output of the engine is a list of `Binding` objects, each containing `BindingMapping` records that map tasks to candidate services.  
   - A `BindingSpace` can be generated to list all possible or feasible bindings.

---

## Architecture & Domain Model

Here is a high-level view of the key classes:

- **`QACOProblem`**:  
  - `compositeWebService`: The set of tasks, candidate services, features, and graph.  
  - `problem`: Defines constraints, optimization preferences, etc.

- **`CompositeWebService`**:  
  - `tasks`: A list of `Task` objects.  
  - `candidateServices`: Potential implementations for tasks.  
  - `features`: QoS or other measurable properties.  
  - `graph`: Nodes and edges describing the process flow.

- **`Problem`**:  
  - `constraints`: A list of constraint objects (Global, Local, Compose, etc.).  
  - `optimization`: Preferences (features, weights) and aggregator operations.

- **`Binding`**:  
  - Maps each `Task` to a chosen `CandidateService`.  
  - A solver returns one or more Bindings as the **optimal solution**.

- **`BindingSpace`**:  
  - Contains a collection of possible `Binding` configurations. It is returned by the engine to explore the solution space.

---

## Installation

### Maven

Add the following dependency to your project’s `pom.xml`:

```xml
<dependency>
    <groupId>com.example.qaco</groupId>
    <artifactId>qaco-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

Then run:
```bash
mvn clean install
```
This will download and link the QACO SDK into your project.

### Gradle

In your `build.gradle`:
```groovy
dependencies {
    implementation 'com.example.qaco:qaco-sdk:1.0.0'
}
```

---

## Usage

### Defining a QACOProblem

```java
import com.example.qaco.domain.QACOProblem;
import com.example.qaco.domain.cws.*;
import com.example.qaco.domain.problem.Problem;

QACOProblem problem = new QACOProblem();

// 1. Create a CompositeWebService
CompositeWebService cws = new CompositeWebService();
Task taskA = new Task();
taskA.setName("TaskA");

CandidateService csA = new CandidateService();
csA.setName("ServiceA");

// Add them to the CWS
cws.setTasks(List.of(taskA));
cws.setCandidateServices(List.of(csA));

// 2. Create a Problem definition
Problem domainProblem = new Problem();
domainProblem.setName("Example Problem");
// Optionally add constraints, optimization, etc.

// 3. Attach them to QACOProblem
problem.setCompositeWebService(cws);
problem.setProblem(domainProblem);
```

### Implementing an Engine

```java
import com.example.qaco.domain.QACOProblem;
import com.example.qaco.domain.binding.Binding;
import com.example.qaco.domain.binding.BindingSpace;
import com.example.qaco.domain.cws.CompositeWebService;
import com.example.qaco.engine.AbstractQACOEngine;

import java.util.List;
import java.util.Optional;

public class MyQACOEngine extends AbstractQACOEngine {

    @Override
    protected Optional<List<Binding>> doSolve(QACOProblem problem) {
        // Custom logic (heuristic, exact algorithm, etc.)
        // Return a list of Binding objects if a solution is found
        return Optional.empty();
    }

    @Override
    protected Optional<BindingSpace> getBindingSpace(CompositeWebService cws) {
        // Generate all feasible bindings for the given CWS
        return Optional.empty();
    }
}
```

### Validations

When you call:

```java
MyQACOEngine engine = new MyQACOEngine();
Optional<List<Binding>> bindings = engine.solve(problem);
```

the **`AbstractQACOEngine`** runs various checks:

1. **CWS** must have at least one task and one candidate service.  
2. **Graph** must have at least one `START` and `END` node (if a graph is provided).  
3. **Constraints** must reference tasks and features actually in the CWS.  
4. **LocalConstraint** cannot have both `value` and `outputFeature` set.  
5. **GlobalConstraint** features must be part of the **Optimization** preferences.  

If any validation fails, an `IllegalArgumentException` or `IllegalStateException` is thrown. In a REST context, you might catch these exceptions and return a `400 Bad Request` with details.

---

## OpenAPI Specification (Optional)

If you want to expose your QACO engine as a **web service**, refer to the provided **OpenAPI** spec (see [`OpenAPI_spec.yaml`](./OpenAPI.yaml)). It describes:

- **POST** `/qaco/solve` to submit a `QACOProblem`.  
- **GET** `/qaco/solve/{jobId}` to retrieve the solution asynchronously.  
- **POST** `/qaco/bindingSpace` to request a `BindingSpace`.  
- **GET** `/qaco/bindingSpace/{jobId}` to retrieve it asynchronously.

The spec also outlines how **validation errors** are reported (`400 Bad Request`) and how to handle long-running jobs (`202 Accepted`). The spec is deployed as a **Swagger UI** at [here](https://app.swaggerhub.com/apis-docs/JAVIERCAVLOP_1/QACO/).

---

## ⚠️ Disclaimer

This project is part of the research activities of the [ISA Group](https://www.isa.us.es/3.0/) and was specifically created as a laborratory package for the paper: "Enabling Replicability in QoS-Aware Web Service Composition: An API-Based Servitization". Please note that the project should be used with caution. We are not responsible for any damage caused by the use of this software. If you find any bugs or have any suggestions, please let us know by opening an issue in the [GitHub repository](https://github.com/isa-group/QACO_SDK).

## 📜 License

This work is licensed under a [Creative Commons Attribution 4.0 International License] [cc-by].

[![CC BY 4.0][cc-by-image]][cc-by]

[cc-by]: http://creativecommons.org/licenses/by/4.0/
[cc-by-image]: https://i.creativecommons.org/l/by/4.0/88x31.png
[cc-by-shield]: https://img.shields.io/badge/License-CC%20BY%204.0-lightgrey.svg