package com.github.mmaico.clone;


import com.github.mmaico.shared.libraries.CollectionUtils;
import com.github.mmaico.shared.libraries.ReflectionUtils;
import com.google.common.collect.Sets;
import com.github.mmaico.clone.node.TreeMirrorNode;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.github.mmaico.clone.node.DestinationNode.newDestNode;
import static com.github.mmaico.clone.node.OriginNode.newOrigin;
import static com.github.mmaico.clone.node.PreviousNode.newPreviousNode;
import static com.github.mmaico.clone.node.TreeMirrorNode.newOrigNode;
import static com.github.mmaico.shared.libraries.CollectionUtils.isCollection;


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

    if (CollectionUtils.isCollection(object) && CollectionUtils.isCollection(hibernateEntity)) {
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
