package com.trex.clone.node;


public class PreviousNode {

  private final Object root;
  private final String field;


  public PreviousNode(Object root, String field) {
    this.root = root;
    this.field = field;
  }

  public static PreviousNode newPreviousNode(Object root, String field) {
    return new PreviousNode(root, field);
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
}
