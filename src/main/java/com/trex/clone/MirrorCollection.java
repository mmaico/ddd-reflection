package com.trex.clone;



import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.trex.clone.reflections.ReflectionCloneUtils;
import com.trex.shared.annotations.EntityReference;
import com.trex.clone.exceptions.InvalidCollectionException;
import com.trex.clone.node.DestinationNode;
import com.trex.clone.node.OriginNode;
import com.trex.clone.node.PreviousNode;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;
import static com.trex.clone.node.PreviousNode.newPreviousNode;
import static com.trex.clone.node.TreeMirrorNode.newOrigNode;
import static com.trex.clone.reflections.ReflectionCloneUtils.isModel;
import static com.trex.shared.libraries.ReflectionUtils.getField;


public class MirrorCollection {


  public void mirror(TreeMirrorNode treeMirrorNode) {

    if (!treeMirrorNode.getOrigin().isClassCollection()) {
      InvalidCollectionException.throwingError(treeMirrorNode.getOrigin());
    }

//    if (!treeMirrorNode.getDest().isClassCollection()) {
//      InvalidCollectionException.throwingError(treeMirrorNode.getOrigin(), treeMirrorNode.getDest());
//    }

    final Collection destinationCollection;

    if (!treeMirrorNode.getDest().collectionIsPresent()) {
        destinationCollection = treeMirrorNode.getDest().generateNewCollection();
    } else {
        destinationCollection = (Collection) treeMirrorNode.getDest().getObject();
    }

    Collection originCollection = (Collection) treeMirrorNode.getOrigin().getObject();

    for (Object itemOrigin: originCollection) {
      com.google.common.base.Optional optional = Iterables.tryFind(destinationCollection, e -> itemOrigin.equals(e));
      final Object found;

      if (!optional.isPresent()) {
        found = ReflectionCloneUtils.newInstance(treeMirrorNode.getPreviousNode().getObject(), treeMirrorNode.getOrigin());
        destinationCollection.add(found);
      } else {
        found = optional.get();
      }

      TreeMirrorNode initialTreeMirrorNode = newOrigNode(newOrigin(itemOrigin, null, null),
            newDestNode(found, Optional.empty(), newPreviousNode(null, null, null)), treeMirrorNode.getNestedObjects());

      new MirrorObject().mirror(initialTreeMirrorNode);
    }

  }

}
