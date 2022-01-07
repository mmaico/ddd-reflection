package com.github.mmaico.dsl.handlers;


import com.github.mmaico.dsl.expressions.BooleanExpression;
import com.github.mmaico.dsl.expressions.ContainsExpression;
import com.github.mmaico.shared.libraries.ReflectionUtils;
import com.github.mmaico.dsl.ConditionalSet;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static com.github.mmaico.shared.libraries.ReflectionUtils.getFieldByGetOrSet;

public class SetInterceptor implements MethodInterceptor {


  private final ConditionalSet conditionalSet;

  public SetInterceptor(ConditionalSet target) {
    this.conditionalSet = target;
  }

  @Override
  public Object intercept(Object objectModel, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {

    if (conditionalSet.getExpression() instanceof BooleanExpression) {
      if (((BooleanExpression)conditionalSet.getExpression()).getMakeSet()) {
        ReflectionUtils.invokeMethod(method, conditionalSet.getTarget(), params);
      }
    } else {
      ContainsExpression expression = (ContainsExpression) conditionalSet.getExpression();
      String fieldName = ReflectionUtils.getFieldByGetOrSet(method);
      if (!expression.isEmpty() && expression.getFields().contains(fieldName)) {
        ReflectionUtils.invokeMethod(method, conditionalSet.getTarget(), params);
      }
    }

    return null;
  }



  public static SetInterceptor create(ConditionalSet conditionalSet) {
    return new SetInterceptor(conditionalSet);
  }

}
