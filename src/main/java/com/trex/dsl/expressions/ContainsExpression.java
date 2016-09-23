package com.trex.dsl.expressions;


import java.util.Set;

public class ContainsExpression implements Expression {

    private final Set<String> fields;

    public ContainsExpression(Set<String> fields) {
        this.fields = fields;
    }

    public Set<String> getFields() {
        return fields;
    }

    public Boolean isEmpty() {
        return fields.isEmpty();
    }

}
