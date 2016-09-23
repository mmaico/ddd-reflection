package com.trex.dsl.expressions;


public class BooleanExpression implements Expression {

    private final Boolean makeSet;

    public BooleanExpression(Boolean makeset) {
        this.makeSet = makeset;
    }

    public Boolean getMakeSet() {
        return makeSet;
    }
}
