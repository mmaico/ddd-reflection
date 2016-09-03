package com.trex.proxy.handlers;


import com.trex.proxy.reflections.ReflectionProxyUtils;
import com.trex.shared.annotations.*;
import com.trex.shared.libraries.ReflectionUtils;
import com.trex.shared.libraries.registers.CollectionsImplementationRegister;
import com.trex.shared.libraries.registers.PrimitiveTypeFields;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.trex.clone.BusinessModelClone.from;
import static com.trex.proxy.reflections.ReflectionProxyUtils.*;
import static com.trex.shared.libraries.CollectionUtils.isCollection;
import static com.trex.shared.libraries.ReflectionUtils.invokeGetter;
import static com.trex.shared.libraries.ReflectionUtils.invokeSetter;

public class SetHandler implements Handler {

    @Override
    public Object handler(HandlerInfoBuilder infoBuilder) {

        Optional<Field> fieldObjectModel = ReflectionProxyUtils.getFieldByGetOrSet(infoBuilder.getObjectModel(), infoBuilder.getMethod().getName());
        Optional<Field> hibernateEntityField = infoBuilder.getHibernateEntityField();

        if (!hibernateEntityField.isPresent()) {
            throw new IllegalArgumentException("Method name [ " + infoBuilder.getMethod().getName() + "] "
                    + "not found on [ " + infoBuilder.getHibernateEntity().getClass() + "]");
        }

        Object hibernateEntity = invokeGetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get());

        Model annotation = null;
        if (!PrimitiveTypeFields.getInstance().contains(hibernateEntityField.get().getType())
                && !(hibernateEntity instanceof Enum)) {
            annotation = infoBuilder.getObjectModel().getClass().getSuperclass().getAnnotation(Model.class);
        }

        boolean isDDDModel = annotation != null;

        if (isDDDModel) {
            if (hibernateEntity != null) {
                from(infoBuilder.getParams(), fieldObjectModel.get()).merge(hibernateEntity);
            } else {
                if (isCollection(fieldObjectModel.get().getType())) {
                    Collection collection = CollectionsImplementationRegister
                            .getInstance().getCollectionInstance(hibernateEntityField.get().getType());

                    Optional<Class> classType = ReflectionUtils.getGenericClassCollection(hibernateEntityField.get());

                    ((Collection)infoBuilder.getParams()).stream()
                            .forEach(item -> collection.add(from(item).convertTo(classType.get())));

                    invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), collection);
                } else {

                    Object resultMerged = from(infoBuilder.getObjectModel()).convertTo(hibernateEntityField.get().getType());
                    invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), resultMerged);
                }

            }
        } else {
            if (fieldObjectModel.get().getAnnotation(Attribute.class) != null
                    && !fieldObjectModel.get().getAnnotation(Attribute.class).converter().isInterface()) {

                Object objectConverted = invokeConverterReverse(fieldObjectModel.get(), infoBuilder.getObjectModel());
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), objectConverted);
            } else {
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), infoBuilder.getParams());

            }
        }

        return null;
    }
}
