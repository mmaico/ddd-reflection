package com.trex.proxy;


import com.trex.proxy.handlers.GetHandler;
import com.trex.proxy.handlers.Handler;
import com.trex.proxy.handlers.HandlerInfoBuilder;
import com.trex.proxy.handlers.SetHandler;
import com.trex.shared.libraries.GetterSetterEnum;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProxyInterceptor implements MethodInterceptor {

  private static final Map<GetterSetterEnum, Handler> handlers = new HashMap<>();

  static {
    handlers.put(GetterSetterEnum.GETTER, new GetHandler());
    handlers.put(GetterSetterEnum.SETTER, new SetHandler());
  }

  private final Object hibernateEntity;
  private Object objectModel;

  public ProxyInterceptor(Object target) {
    this.hibernateEntity = target;
  }

  @Override
  public Object intercept(Object objectModel, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
    this.objectModel = objectModel;
    HandlerInfoBuilder builder = HandlerInfoBuilder.create(objectModel, method, params, hibernateEntity);

    if (!builder.isGetOrSetOperation() || hibernateEntity == null) {
      return methodProxy.invokeSuper(objectModel, params);
    }

    return handlers.get(builder.getOperation()).handler(builder);
  }

  public Object getHibernateEntity() {
    return hibernateEntity;
  }

  public Object getObjectModel() {
    return objectModel;
  }

  public static ProxyInterceptor create(Object object) {
    return new ProxyInterceptor(object);
  }

  public String toString() {
    return "teste";
  }
}
