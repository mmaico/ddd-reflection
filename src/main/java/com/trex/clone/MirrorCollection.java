package com.trex.clone;



import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.trex.shared.annotations.EntityReference;
import com.trex.clone.exceptions.InvalidCollectionException;
import com.trex.clone.node.DestinationNode;
import com.trex.clone.node.OriginNode;
import com.trex.clone.node.PreviousNode;
import com.trex.clone.node.TreeMirrorNode;
import com.trex.shared.libraries.ReflectionUtils;

import java.util.Collection;
import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;
import static com.trex.clone.node.PreviousNode.newPreviousNode;
import static com.trex.clone.node.TreeMirrorNode.newOrigNode;


public class MirrorCollection {


  public void mirror(TreeMirrorNode treeMirrorNode) {

    if (!treeMirrorNode.getOrigin().isClassCollection()) {
      InvalidCollectionException.throwingError(treeMirrorNode.getOrigin());
    }

    if (!treeMirrorNode.getDest().isClassCollection()) {
      InvalidCollectionException.throwingError(treeMirrorNode.getOrigin(), treeMirrorNode.getDest());
    }

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
        EntityReference annotation = treeMirrorNode.getOrigin().getField().getAnnotation(EntityReference.class);
        found = ReflectionUtils.newInstance(annotation.value());
        destinationCollection.add(found);
      } else {
        found = optional.get();
      }

      TreeMirrorNode initialTreeMirrorNode = newOrigNode(newOrigin(itemOrigin, null),
            newDestNode(found, Optional.empty(), newPreviousNode(null, null)), treeMirrorNode.getNestedObjects());

      new MirrorObject().mirror(initialTreeMirrorNode);
    }

  }

}
