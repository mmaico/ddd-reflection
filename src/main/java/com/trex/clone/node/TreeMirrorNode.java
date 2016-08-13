package com.trex.clone.node;


import com.google.common.collect.Sets;

import java.util.Optional;
import java.util.Set;

public class TreeMirrorNode {

  private final OriginNode origin;
  private final DestinationNode target;

  /**
   * Nested objects copied are stored to invict circular reference
   * and a stack overflow
   */
  private final Set<NestedObjectCopied> nestedObjects = Sets.newHashSet();


  public TreeMirrorNode(OriginNode origin, DestinationNode target, Set<NestedObjectCopied> nestedObjects) {
    this.origin = origin;
    this.target = target;
    this.nestedObjects.add(NestedObjectCopied.createNestedCopied(origin.getObject(), target.getObject()));
    this.nestedObjects.addAll(nestedObjects);
  }

  public Boolean hasDestination() {
    return this.target.getObject() != null;
  }

  public OriginNode getOrigin() {
    return origin;
  }

  public DestinationNode getDest() {
    return target;
  }

  public PreviousNode getPreviousNode() {
    return target.getPreviousNode();
  }

  public Set<NestedObjectCopied> getNestedObjects() {
    return nestedObjects;
  }

  public Boolean hasNestedObjectsCopied(Object object) {
    return nestedObjects.contains(object);
  }

  public Optional<NestedObjectCopied> getBy(Object origin) {
    return  nestedObjects.stream().filter(item -> item.getOrigin().equals(origin)).findFirst();
  }

  public static TreeMirrorNode newOrigNode(OriginNode origin, DestinationNode target, Set<NestedObjectCopied> nestedObjects) {
    return new TreeMirrorNode(origin, target, nestedObjects);
  }

}
