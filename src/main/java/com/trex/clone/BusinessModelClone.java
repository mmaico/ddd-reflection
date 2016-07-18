package com.trex.clone;




import com.trex.clone.node.PreviousNode;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.CollectionUtils;
import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;
import static com.trex.shared.libraries.CollectionUtils.isCollection;


public class BusinessModelClone {

  private Object object;
  private Field fieldModel;

  public BusinessModelClone(Object object) {
    this.object = object;
  }
  public BusinessModelClone(Object object, Field fieldModel) {
    this.object = object;
    this.fieldModel = fieldModel;
  }

  public static BusinessModelClone from(Object object) {
      return new BusinessModelClone(object);
  }

  public static BusinessModelClone from(Object object, Field field) {
    return new BusinessModelClone(object, field);
  }

  public void merge(Object hibernateEntity) {

    TreeMirrorNode initialTreeMirrorNode = TreeMirrorNode.newOrigNode(newOrigin(object, this.fieldModel), newDestNode(hibernateEntity,
            Optional.empty(), PreviousNode.newPreviousNode(null, null)));

    if (isCollection(object) && isCollection(hibernateEntity)) {
      new MirrorCollection().mirror(initialTreeMirrorNode.getOrigin(), initialTreeMirrorNode.getDest());
    } else {
      new MirrorObject().mirror(initialTreeMirrorNode);
    }

  }
  public <T> T convertTo(Class<T> clazz) {
    Object target = ReflectionUtils.newInstance(clazz);

    TreeMirrorNode initialTreeMirrorNode = TreeMirrorNode.newOrigNode(newOrigin(object, null), newDestNode(target,
        Optional.empty(), PreviousNode.newPreviousNode(null, null)));

    new MirrorObject().mirror(initialTreeMirrorNode);

    return (T) target;
  }
}
