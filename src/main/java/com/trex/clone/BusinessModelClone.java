package com.trex.clone;




import com.trex.clone.node.PreviousNode;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;


public class BusinessModelClone {

  private Object object;

  public BusinessModelClone(Object object) {
    this.object = object;
  }

  public static BusinessModelClone from(Object object) {
      return new BusinessModelClone(object);
  }

  public <T> T convertTo(Class<T> clazz) {
    Object target = ReflectionUtils.newInstance(clazz);

    TreeMirrorNode initialTreeMirrorNode = TreeMirrorNode.newOrigNode(newOrigin(object, null), newDestNode(target,
        Optional.empty(), PreviousNode.newPreviousNode(null, null)));

    new MirrorObject().mirror(initialTreeMirrorNode);

    return (T) target;
  }
}
