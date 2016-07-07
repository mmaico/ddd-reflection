package com.trex.proxy;



import com.trex.proxy.reflections.ReflectionProxyUtils;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;
import com.trex.shared.libraries.ReflectionUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

import static com.trex.proxy.reflections.ReflectionProxyUtils.invokeCustomConverter;

public class ProxyHandler implements MethodInterceptor {

  private final Object hibernateEntity;

  public ProxyHandler(Object target) {
    this.hibernateEntity = target;
  }

  @Override
  public Object intercept(Object objectModel, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {

    if (hibernateEntity == null) {
      return methodProxy.invokeSuper(objectModel, objects);
    }

    if (method.getAnnotation(Extractor.class) != null) {
      return ReflectionProxyUtils.invokeExtractor(method, hibernateEntity);
    }

    String methodName = method.getName();

    String hibernateEntityField = ReflectionProxyUtils.getHibernateEntityFieldNameBy(objectModel, methodName);
    Optional<Field> hibernateEntityFieldFound = ReflectionUtils.getField(this.hibernateEntity, hibernateEntityField);

    if (!hibernateEntityFieldFound.isPresent()) {
      throw new IllegalArgumentException("Attribute name [ " + hibernateEntityField +"] "
          + "not found on [ " + this.hibernateEntity.getClass() + "]");
    }

    boolean isDDDModel = ReflectionUtils.hasAnnotation(method.getReturnType(), EntityReference.class);

    if (isDDDModel) {
      Object result = ReflectionUtils.getValue(this.hibernateEntity, hibernateEntityFieldFound.get());
      return Enhancer.create(method.getReturnType(), ProxyHandler.create(result));
    } else {
      if (ReflectionProxyUtils.hasCustomConverter(objectModel, methodName)) {
        Field fieldObjectModel = ReflectionProxyUtils.getFieldByMethodName(objectModel, methodName);
        return invokeCustomConverter(this.hibernateEntity, hibernateEntityFieldFound.get(), fieldObjectModel);
      } else {
        return ReflectionUtils.getValue(this.hibernateEntity, hibernateEntityFieldFound.get());
      }
    }

  }

  public static ProxyHandler create(Object object) {
    return new ProxyHandler(object);
  }
}
