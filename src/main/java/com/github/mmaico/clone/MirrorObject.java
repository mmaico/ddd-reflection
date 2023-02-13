package com.github.mmaico.clone;


import com.github.mmaico.clone.node.*;

import java.util.List;
import java.util.Optional;

import static com.github.mmaico.clone.node.DestinationNode.newDestNode;
import static com.github.mmaico.clone.node.OriginNode.newOrigin;
import static com.github.mmaico.clone.node.PreviousNode.newPreviousNode;
import static com.github.mmaico.clone.node.TreeMirrorNode.newOrigNode;
import static com.github.mmaico.clone.reflections.ReflectionCloneUtils.*;

/**
 * Class responsible to copy objects.
 */
public class MirrorObject {


    public void mirror(TreeMirrorNode treeMirrorNode) {
        final PreviousNode previousNode = treeMirrorNode.getPreviousNode();

        if (!treeMirrorNode.hasDestination()) {
            Optional<Object> newInstanceDestNode = treeMirrorNode.getOrigin().generateNewInstanceDestination(treeMirrorNode.getPreviousNode().getObject());

            if (newInstanceDestNode.isPresent()) {
                invokeSetter(previousNode.getObject(), treeMirrorNode.getOrigin(), newInstanceDestNode.get());
                Optional<String> destField = Optional.ofNullable(treeMirrorNode.getOrigin().getField());

                TreeMirrorNode nextTreeMirrorNode = newOrigNode(treeMirrorNode.getOrigin(),
                        newDestNode(newInstanceDestNode.get(), destField, previousNode), treeMirrorNode.getNestedObjects());

                mirror(nextTreeMirrorNode);
            }
        } else {
            mergePrimitiveAttributes(treeMirrorNode.getOrigin().getObject(), treeMirrorNode.getDest().getObject());
            mergeAttrWithCustomConverter(treeMirrorNode.getOrigin().getObject(), treeMirrorNode.getDest().getObject());
            Object modelObject = getModel(treeMirrorNode.getOrigin().getObject(), treeMirrorNode.getDest().getObject());

            List<ChildNode> children = getChildren(modelObject);

            for (ChildNode child : children) {
                Object result = invokeGetter(treeMirrorNode.getOrigin().getObject(), child);
                OriginNode originNode = newOrigin(result, child.getField(), child.getFieldModelName());

                if (originNode.isNull()) continue;

                Optional<NestedObjectCopied> nestedObjectCopiedFound = treeMirrorNode.getBy(result);

                if (nestedObjectCopiedFound.isPresent()) {
                    Object destination = nestedObjectCopiedFound.get().getDestination();
                    invokeSetter(treeMirrorNode.getDest().getObject(), child, destination);
                    continue;
                }

                TreeMirrorNode nextTreeMirrorNode = createTreeMirrorNode(treeMirrorNode, child, originNode);

                if (nextTreeMirrorNode.getOrigin().isClassCollection()) {
                    new MirrorCollection().mirror(nextTreeMirrorNode);
                } else {
                    mirror(nextTreeMirrorNode);
                }
            }
        }
    }

    private TreeMirrorNode createTreeMirrorNode(TreeMirrorNode treeMirrorNode, ChildNode child, OriginNode originNode) {
        Object destChild = invokeGetter(treeMirrorNode.getDest().getObject(), child.getField());

        PreviousNode previousNodeChild = newPreviousNode(treeMirrorNode.getDest().getObject(), originNode.getField(), child.getFieldModelName());
        DestinationNode destinationNode = newDestNode(destChild, Optional.ofNullable(child.getField()), previousNodeChild);

        return newOrigNode(originNode, destinationNode, treeMirrorNode.getNestedObjects());
    }

}
