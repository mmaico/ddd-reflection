package com.github.mmaico.clone;



import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import com.github.mmaico.clone.reflections.ReflectionCloneUtils;
import com.github.mmaico.clone.exceptions.InvalidCollectionException;
import com.github.mmaico.clone.node.TreeMirrorNode;

import java.util.Collection;
import java.util.Optional;

import static com.github.mmaico.clone.node.DestinationNode.newDestNode;
import static com.github.mmaico.clone.node.OriginNode.newOrigin;
import static com.github.mmaico.clone.node.PreviousNode.newPreviousNode;
import static com.github.mmaico.clone.node.TreeMirrorNode.newOrigNode;
import static com.github.mmaico.shared.libraries.ReflectionUtils.getField;


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
