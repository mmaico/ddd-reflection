package com.github.mmaico.proxy;


import net.sf.cglib.proxy.Enhancer;

public class BusinessModelProxy {

  private Object object;

  public BusinessModelProxy(Object object) {
    this.object = object;
  }

  public static BusinessModelProxy from(Object object) {
    return new BusinessModelProxy(object);
  }

  public <T> T proxy(Class<T> clazz) {
    ProxyInterceptor proxyInterceptor = ProxyInterceptor.create(object);
    return  (T) Enhancer.create(clazz, proxyInterceptor);
  }
}
