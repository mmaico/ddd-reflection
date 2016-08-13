package com.trex.clone;


import com.trex.clone.node.*;
import com.trex.clone.reflections.ReflectionCloneUtils;
import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static com.trex.clone.node.DestinationNode.newDestNode;
import static com.trex.clone.node.OriginNode.newOrigin;
import static com.trex.clone.node.PreviousNode.newPreviousNode;
import static com.trex.clone.node.TreeMirrorNode.newOrigNode;
import static com.trex.clone.reflections.ReflectionCloneUtils.*;
import static com.trex.shared.libraries.ReflectionUtils.invokeSetter;

/**
 * Class responsible to copy objects.
 */
public class MirrorObject {


    public void mirror(TreeMirrorNode treeMirrorNode) {
        final PreviousNode previousNode = treeMirrorNode.getPreviousNode();

        if (!treeMirrorNode.hasDestination()) {
            Optional<Object> newInstanceDestNode = treeMirrorNode.getOrigin().generateNewInstanceDestination();

            if (newInstanceDestNode.isPresent()) {
                invokeSetter(previousNode.getObject(), previousNode.getField(), newInstanceDestNode.get());
                Optional<Field> destField = getDestField(newInstanceDestNode.get(), treeMirrorNode.getOrigin().getField());

                TreeMirrorNode nextTreeMirrorNode = newOrigNode(treeMirrorNode.getOrigin(),
                        newDestNode(newInstanceDestNode.get(), destField, previousNode), treeMirrorNode.getNestedObjects());

                mirror(nextTreeMirrorNode);
            }
        } else {
            mergePrimitiveAttributes(treeMirrorNode.getOrigin().getObject(), treeMirrorNode.getDest().getObject());
            mergeAttrWithCustomConverter(treeMirrorNode.getOrigin().getObject(), treeMirrorNode.getDest().getObject());
            List<ChildNode> children = ReflectionCloneUtils.getReferenceFields(treeMirrorNode.getOrigin().getObject());

            for (ChildNode child : children) {
                OriginNode originNode = newOrigin(child.getObject(), child.getField());
                if (originNode.isNull()) continue;

                Optional<NestedObjectCopied> nestedObjectCopiedFound = treeMirrorNode.getBy(child.getObject());

                if (nestedObjectCopiedFound.isPresent()) {
                    String fieldNameDestination = originNode.getAttributeNameToDestination();
                    Object destination = nestedObjectCopiedFound.get().getDestination();
                    invokeSetter(treeMirrorNode.getDest().getObject(), fieldNameDestination, destination);
                    continue;
                }

                TreeMirrorNode nextTreeMirrorNode = createTreeMirrorNode(treeMirrorNode, previousNode, child, originNode);

                if (nextTreeMirrorNode.getOrigin().isClassCollection()) {
                    new MirrorCollection().mirror(nextTreeMirrorNode);
                } else {
                    mirror(nextTreeMirrorNode);
                }
            }
        }
    }

    private TreeMirrorNode createTreeMirrorNode(TreeMirrorNode treeMirrorNode, PreviousNode previousNode, ChildNode child, OriginNode originNode) {
        Object destChild = invokeGetter(treeMirrorNode.getDest().getObject(), child.getField());

        PreviousNode previousNodeChild = newPreviousNode(treeMirrorNode.getDest().getObject(), originNode.getAttributeNameToDestination());
        DestinationNode destinationNode = newDestNode(destChild, getDestField(previousNode.getObject(), child.getField()), previousNodeChild);

        return newOrigNode(originNode, destinationNode, treeMirrorNode.getNestedObjects());
    }

}
