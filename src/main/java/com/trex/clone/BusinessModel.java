package com.trex.clone;




import com.trex.clone.node.PreviousNode;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;


public class BusinessModel {

  private Object object;

  public BusinessModel(Object object) {
    this.object = object;
  }

  public static BusinessModel from(Object object) {
      return new BusinessModel(object);
  }

  public <T> T convertTo(Class<T> clazz) {
    Object target = ReflectionUtils.newInstance(clazz);

    TreeMirrorNode initialTreeMirrorNode = TreeMirrorNode.newOrigNode(newOrigin(object, null), newDestNode(target,
        Optional.empty(), PreviousNode.newPreviousNode(null, null)));

    new MirrorObject().mirror(initialTreeMirrorNode);

    return (T) target;
  }
}
