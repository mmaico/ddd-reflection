package com.trex.clone.node;


import java.lang.reflect.Field;

public class ChildNode {

  private final Field field;
  private final Object object;

  public ChildNode(Object object, Field field) {
    this.field = field;
    this.object = object;
  }

  public static ChildNode createDescriptor(Object object, Field field) {
      return new ChildNode(object, field);
  }

  public Field getField() {
    return field;
  }

  public Object getObject() {
    return object;
  }
}
