package com.trex.clone.node;


public class ChildNode implements NodeFields {

  private final String field;
  private final String fieldModelName;

  public ChildNode(String field, String fieldModelName) {
    this.field = field;
    this.fieldModelName = fieldModelName;

  }

  public static ChildNode createDescriptor(String field, String fieldModelName) {
      return new ChildNode(field, fieldModelName);
  }

  public String getField() {
    return field;
  }

  public String getFieldModelName() {
    return fieldModelName;
  }

}
