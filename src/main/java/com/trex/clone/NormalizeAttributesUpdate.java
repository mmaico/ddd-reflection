package com.trex.clone;


import com.trex.shared.annotations.UpdateAttributes;
import com.trex.shared.libraries.ReflectionUtils;
import com.trex.shared.libraries.registers.PrimitiveTypeFields;
import org.azeckoski.reflectutils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.trex.shared.libraries.ReflectionUtils.invokeGetter;

public class NormalizeAttributesUpdate {


    @SuppressWarnings("rawtypes")
    public void addFieldsToUpdate(Object entity, Set<String> requestAttributes) {

        for (String attr : requestAttributes) {
            try {
                if (!attr.matches(".+\\..+")) {
                    addAttribute(entity, attr);
                } else {
                    doObjectReference(entity, attr);

                    int lastIndexOf = attr.lastIndexOf(".");
                    String prefix = attr.substring(0, lastIndexOf);
                    String fieldName = attr.substring(++lastIndexOf, attr.length());

                    Object nestedEntity = ReflectUtils.getInstance().getFieldValue(entity, prefix);
                    if (nestedEntity != null) {
                        String fiedNameTreat = fieldName.trim().replaceAll("\\[.+\\]", "");
                        addAttribute(nestedEntity, fiedNameTreat);
                    }
                }

            } catch (Exception e) {}
        }
    }

    /**
     * adds referece objects for updating
     *
     */
    @SuppressWarnings("rawtypes")
    private void doObjectReference(Object entity, String path) {

        int firstIndexOf = path.indexOf(".");
        String prefix = path.substring(0, firstIndexOf);
        String suffix = path.substring(firstIndexOf + 1, path.length());

        Collection fieldsList = getAttrCollection(entity);
        fieldsList.add(prefix.replaceAll("\\[.+?\\]", ""));

        if (suffix.matches(".+\\..+")) {
            Object object = ReflectUtils.getInstance().getFieldValue(entity, prefix);

            if ( object != null && !PrimitiveTypeFields.getInstance().contains(object.getClass())) {
                doObjectReference(object, suffix);
            }

        }

    }

    private void addAttribute(Object entity, String value) {
        Optional<Field> updateAttributes = ReflectionUtils.getField(entity, UpdateAttributes.class);

        if (updateAttributes.isPresent()) {
            Object attributesCollection = invokeGetter(entity, updateAttributes.get());

            if (attributesCollection instanceof Collection) {
                ((Collection)attributesCollection).add(value.trim().replaceAll("\\[.+\\]", ""));
            }
        }
    }

    private Collection getAttrCollection(Object entity) {
        Optional<Field> updateAttributes = ReflectionUtils.getField(entity, UpdateAttributes.class);

        Object attributesCollection = invokeGetter(entity, updateAttributes.get());
        if (attributesCollection instanceof Collection) {
            return (Collection) attributesCollection;
        } else {
            throw new IllegalArgumentException("@UpdateAttributes are not a Collection ["+ entity + "]");
        }
    }

}
