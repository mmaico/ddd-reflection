package com.trex.proxy;


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
    ProxyHandler proxyHandler = ProxyHandler.create(object);
    return  (T) Enhancer.create(clazz, proxyHandler);
  }
}
