package com.trex.clone;



import com.google.common.collect.Iterables;
import com.trex.clone.annotations.EntityReference;
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



public class MirrorCollection {


  public void mirror(OriginNode originNode, DestinationNode destinationNode) {

    if (!originNode.isClassCollection()) {
      InvalidCollectionException.throwingError(originNode);
    }

    if (!destinationNode.isClassCollection()) {
      InvalidCollectionException.throwingError(originNode, destinationNode);
    }

    final Collection destinationCollection;

    if (!destinationNode.collectionIsPresent()) {
        destinationCollection = destinationNode.generateNewCollection();
    } else {
        destinationCollection = (Collection) destinationNode.getObject();
    }

    Collection originCollection = (Collection) originNode.getObject();

    for (Object itemOrigin: originCollection) {

      com.google.common.base.Optional optional = Iterables.tryFind(destinationCollection, e -> e.equals(itemOrigin));
      final Object found;

      if (!optional.isPresent()) {
        EntityReference annotation = originNode.getField().getAnnotation(EntityReference.class);
        found = ReflectionUtils.newInstance(annotation.value());
        destinationCollection.add(found);
      } else {
        found = optional.get();
      }

      TreeMirrorNode initialTreeMirrorNode = TreeMirrorNode.newOrigNode(newOrigin(itemOrigin, null),
            newDestNode(found, Optional.empty(), PreviousNode.newPreviousNode(null, null)));

      new MirrorObject().mirror(initialTreeMirrorNode);
    }

  }

}
