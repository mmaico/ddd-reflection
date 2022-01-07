package com.github.mmaico.shared.libraries.registers;


import com.github.mmaico.shared.libraries.ReflectionUtils;

import java.util.*;

public class CollectionsImplementationRegister {

  private static final Map<Class<? extends Collection>, Class<? extends Collection>>
      collectionImplementations = new HashMap<>();

  static {
    collectionImplementations.put(List.class, ArrayList.class);
    collectionImplementations.put(Set.class, HashSet.class);
    collectionImplementations.put(Queue.class, LinkedList.class);
  }

  public static CollectionsImplementationRegister getInstance() {
    return new CollectionsImplementationRegister();
  }

  public Class<? extends Collection> getCollectionImpl(Class classInterface) {
    return collectionImplementations.get(classInterface);
  }

  public Collection getCollectionInstance(Class classInterface) {
    Class<? extends Collection> aClass = collectionImplementations.get(classInterface);

    return (Collection) ReflectionUtils.newInstance(aClass);
  }
}
