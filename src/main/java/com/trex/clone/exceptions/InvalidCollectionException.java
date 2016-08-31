package com.trex.clone.exceptions;



import com.trex.clone.node.DestinationNode;
import com.trex.clone.node.OriginNode;

public class InvalidCollectionException extends RuntimeException {

  /**
   *
   */
  private static final long serialVersionUID = 1L;



  public InvalidCollectionException(OriginNode originNode) {
    super("Could not find a collection in origin ["
        + originNode.getField() + "] on target [" + originNode.getField() + "]");
  }

  public InvalidCollectionException(OriginNode originNode, DestinationNode destinationNode) {
    super("Could not find a collection on destination [ " + destinationNode.getPreviousNode().getObject() + " ]"
        + " calling destination method [ "  + originNode.getField() + " ]");
  }

  public static void throwingError(OriginNode node) {
    new InvalidCollectionException(node);
  }

  public static void throwingError(OriginNode originNode, DestinationNode destinationNode) {
    new InvalidCollectionException(originNode, destinationNode);
  }

}
