package com.trex.clone;


import com.google.common.collect.Sets;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;
import static com.trex.clone.node.PreviousNode.newPreviousNode;
import static com.trex.clone.node.TreeMirrorNode.newOrigNode;
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

    TreeMirrorNode initialTreeMirrorNode = newOrigNode(newOrigin(object, null, null), newDestNode(hibernateEntity,
            Optional.empty(), newPreviousNode(null, null, null)), Sets.newHashSet());

    if (isCollection(object) && isCollection(hibernateEntity)) {
      TreeMirrorNode treeNode = newOrigNode(initialTreeMirrorNode.getOrigin(),
              initialTreeMirrorNode.getDest(), initialTreeMirrorNode.getNestedObjects());

      new MirrorCollection().mirror(treeNode);
    } else {
      new MirrorObject().mirror(initialTreeMirrorNode);
    }

  }
  public <T> T convertTo(Class<T> clazz) {
    Object target = ReflectionUtils.newInstance(clazz);

    TreeMirrorNode initialTreeMirrorNode = newOrigNode(newOrigin(object, null, null), newDestNode(target,
        Optional.empty(), newPreviousNode(null, null, null)), Sets.newHashSet());

    new MirrorObject().mirror(initialTreeMirrorNode);

    return (T) target;
  }
}
