package com.trex.clone.node;


public class PreviousNode implements NodeFields {

  private final Object root;
  private final String field;
  private final String fieldModelName;


  public PreviousNode(Object root, String field, String fieldModelName) {
    this.root = root;
    this.field = field;
    this.fieldModelName = fieldModelName;
  }

  public static PreviousNode newPreviousNode(Object root, String field, String fieldModelName) {
    return new PreviousNode(root, field, fieldModelName);
  }

  public Object getObject() {
    return root;
  }

  public String getField() {
    return field;
  }

  public Boolean isNull() {
    return root == null;
  }


  public String getFieldModelName() {
    return fieldModelName;
  }
}
