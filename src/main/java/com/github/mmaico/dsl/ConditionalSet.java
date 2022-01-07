package com.github.mmaico.dsl;


import com.github.mmaico.dsl.expressions.BooleanExpression;
import com.github.mmaico.dsl.expressions.ContainsExpression;
import com.github.mmaico.dsl.expressions.Expression;
import com.github.mmaico.dsl.handlers.SetInterceptor;
import net.sf.cglib.proxy.Enhancer;

import java.util.Set;

public class ConditionalSet<T> {

    private T target;
    private Expression expression;

    public ConditionalSet(T target) {
        this.target = target;
    }

    public T ifPresent(Set<String> fields) {
        this.expression = new ContainsExpression(fields);

        return (T) Enhancer.create(target.getClass(), SetInterceptor.create(this));
    }

    public T when(Boolean whenTrue) {
        this.expression = new BooleanExpression(whenTrue);

        return (T) Enhancer.create(target.getClass(), SetInterceptor.create(this));
    }

    public T getTarget() {
        return target;
    }

    public Expression getExpression() {
        return expression;
    }

    public static <T> ConditionalSet<T> set(T target) {
        return new ConditionalSet<>(target);
    }
}
