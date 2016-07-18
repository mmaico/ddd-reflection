package com.trex.proxy.handlers;


import com.trex.proxy.reflections.ReflectionProxyUtils;
import com.trex.shared.annotations.CustomConverter;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;
import com.trex.shared.libraries.registers.CollectionsImplementationRegister;

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

        if (infoBuilder.getMethod().getAnnotation(Extractor.class) != null) {
            Object valueConverted = invokeExtractorReverse(infoBuilder.getMethod(), infoBuilder.getObjectModel());
            invokeSetter(infoBuilder.getHibernateEntity(), infoBuilder.getHibernateEntityField().get(), valueConverted);
        }

        Optional<Field> hibernateEntityField = infoBuilder.getHibernateEntityField();

        if (!hibernateEntityField.isPresent()) {
            throw new IllegalArgumentException("Method name [ " + infoBuilder.getMethod().getName() + "] "
                    + "not found on [ " + infoBuilder.getHibernateEntity().getClass() + "]");
        }

        Object hibernateEntity = invokeGetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get());
        Field fieldObjectModel = getFieldByMethodName(infoBuilder.getObjectModel(), infoBuilder.getMethod().getName());
        boolean isDDDModel = fieldObjectModel.getAnnotation(EntityReference.class) != null;

        if (isDDDModel) {
            if (hibernateEntity != null) {
                from(infoBuilder.getParams(), fieldObjectModel).merge(hibernateEntity);
            } else {
                EntityReference annotation = fieldObjectModel.getAnnotation(EntityReference.class);
                if (isCollection(fieldObjectModel.getType())) {
                    Collection collection = CollectionsImplementationRegister
                            .getInstance().getCollectionInstance(hibernateEntityField.get().getType());

                    ((Collection)infoBuilder.getParams()).stream()
                            .forEach(item -> collection.add(from(item).convertTo(annotation.value())));

                    invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), collection);
                } else {
                    Object resultMerged = from(infoBuilder.getObjectModel()).convertTo(annotation.value());
                    invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), resultMerged);
                }

            }
        } else {
            if (fieldObjectModel.getAnnotation(CustomConverter.class) != null) {
                Object objectConverted = invokeCustomConverterReverse(fieldObjectModel, infoBuilder.getParams());
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), objectConverted);
            } else {
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), infoBuilder.getParams());

            }
        }

        return null;
    }
}
