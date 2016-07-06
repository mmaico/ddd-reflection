package com.trex.clone.node;


public class TreeMirrorNode {

  private final OriginNode origin;
  private final DestinationNode target;


  public TreeMirrorNode(OriginNode origin, DestinationNode target) {
    this.origin = origin;
    this.target = target;
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

  public static TreeMirrorNode newOrigNode(OriginNode origin, DestinationNode target) {
    return new TreeMirrorNode(origin, target);
  }

}
