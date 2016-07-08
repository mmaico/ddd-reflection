package com.trex.proxy;



import com.trex.shared.libraries.ReflectionUtils;
import com.trex.shared.libraries.registers.PrimitiveTypeFields;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProxyCollectionHandler {

  private final Collection hibernateCollection;
  private final Field fieldOrigin;

  public ProxyCollectionHandler(Collection target, Field fieldOrigin) {
    this.hibernateCollection = target;
    this.fieldOrigin = fieldOrigin;
  }

  public Object proxy() {
    Optional<Class> genericClassCollection = ReflectionUtils.getGenericClassCollection(fieldOrigin);

    if (!genericClassCollection.isPresent()) {
      throw new IllegalArgumentException("Invalid collection on field [" + fieldOrigin.getName() + "]");
    }

    Boolean hasPrimitive = PrimitiveTypeFields.getInstance().contains(genericClassCollection.get());

    if (hasPrimitive) {
      return hibernateCollection;
    } else {
      return this.hibernateCollection.stream()
          .map(item ->
              Enhancer.create(genericClassCollection.get(), ProxyHandler.create(item))
          ).collect(Collectors.toList());
    }
  }

  public static ProxyCollectionHandler createProxyCollection(Collection collection, Field fieldOrigin) {
    return new ProxyCollectionHandler(collection, fieldOrigin);
  }
}
