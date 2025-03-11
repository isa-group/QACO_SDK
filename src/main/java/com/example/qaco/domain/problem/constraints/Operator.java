package com.example.qaco.domain.problem.constraints;

public enum Operator {
    LESS_THAN_OR_EQUALS("<="),
    LESS_THAN("<"),
    EQUALS("="),
    GREATER_THAN(">"),
    GREATER_THAN_OR_EQUALS(">="),
    NOT_EQUALS("!=");

    private final String symbol;

    Operator(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol;
    }
}
