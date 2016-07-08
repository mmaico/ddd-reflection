package com.trex.clone.node;




import com.trex.shared.libraries.ReflectionUtils;
import com.trex.shared.libraries.registers.CollectionsImplementationRegister;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class DestinationNode {

    private final Object target;
    private final Optional<Field> field;
    private final PreviousNode previousNode;


    public DestinationNode(Object object, Optional<Field> field, PreviousNode previousNode) {
        this.target = object;
        this.field = field;
        this.previousNode = previousNode;
    }

    public Object getObject() {
        return target;
    }

    public static DestinationNode newDestNode(Object objectDest, Optional<Field> field, PreviousNode previousNode) {
        return new DestinationNode(objectDest, field, previousNode);
    }

    public Boolean isNull() {
        return target == null;
    }

    public boolean isClassCollection() {
        return  field.isPresent() && (Collection.class.isAssignableFrom(field.get().getType()) || Map.class.isAssignableFrom(field.get().getType()));
    }

    public Optional<Field> getField() {
        return field;
    }

    public PreviousNode getPreviousNode() {
        return previousNode;
    }

    public boolean collectionIsPresent() {
        return target != null;
    }

    public Collection generateNewCollection() {

        Optional<Field> field = ReflectionUtils.getField(this.previousNode.getObject(), previousNode.getField());
        if (!field.isPresent()) {
            throw new RuntimeException("Field not found on: [ " + this.previousNode.getObject() + " ] field [ " + previousNode.getField() + " ]");
        }
        Class<? extends Collection> collectionImpl =
            CollectionsImplementationRegister.getInstance().getCollectionImpl(field.get().getType());

        if (collectionImpl == null) {
            throw new RuntimeException("Collection impl not found : [ " + this.previousNode.getObject() + " ] field [ " + previousNode.getField() + " ]");
        }

        Collection collection = (Collection) ReflectionUtils.newInstance(collectionImpl);

        ReflectionUtils.invokeSetter(this.previousNode.getObject(), this.previousNode.getField(), collection);
        return collection;
    }

}
