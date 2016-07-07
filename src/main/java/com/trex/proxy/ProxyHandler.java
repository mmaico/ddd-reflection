package com.trex.proxy;



import com.trex.shared.annotations.EntityReference;
import com.trex.shared.libraries.ReflectionUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class ProxyHandler implements MethodInterceptor {

  private final Object object;

  public ProxyHandler(Object target) {
    this.object = target;
  }

  @Override
  public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
    String methodName = method.getName();
    Method hibernateEntity = ReflectionUtils.findMethod(object.getClass(), methodName);

    if (hibernateEntity == null) {
      return methodProxy.invokeSuper(o, objects);
    }

    boolean isDDDModel = ReflectionUtils.hasAnnotation(method.getReturnType(), EntityReference.class);

    if (isDDDModel) {
      Object result = ReflectionUtils.invokeMethod(object, methodName);
      return Enhancer.create(method.getReturnType(), ProxyHandler.create(result));
    } else {
      return ReflectionUtils.invokeMethod(object, methodName);
    }

  }

  public static ProxyHandler create(Object object) {
    return new ProxyHandler(object);
  }
}
