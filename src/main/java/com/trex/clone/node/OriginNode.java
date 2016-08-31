package com.trex.clone.node;


import com.trex.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

import static com.trex.clone.reflections.ReflectionCloneUtils.isModel;
import static com.trex.shared.libraries.CollectionUtils.isCollection;


public class OriginNode implements NodeFields {

    private final String field;
    private final String fieldModelName;
    private final Object origin;

    public OriginNode(String field, Object origin, String fieldModelName) {
        this.field = field;
        this.origin = origin;
        this.fieldModelName = fieldModelName;
    }

    public Optional<Object> generateNewInstanceDestination(Object previousObject) {
        String fieldToFind = isModel(previousObject) ? this.fieldModelName : field;

        Optional<Field> fieldFound = ReflectionUtils.getField(previousObject, fieldToFind);
        if (!fieldFound.isPresent()) return Optional.empty();
        return Optional.ofNullable(ReflectionUtils.newInstance(fieldFound.get().getType()));
    }

    public boolean isClassCollection() {
        return isCollection(origin);
    }

    public Object getObject() {
        return origin;
    }

    public String getField() {
        return field;
    }

    public String getFieldModelName() {
        return fieldModelName;
    }

    public static OriginNode newOrigin(Object object, String field, String fieldModelName) {
        return new OriginNode(field, object, fieldModelName);
    }

    public boolean isNull() {
        return getObject() == null;
    }
}
