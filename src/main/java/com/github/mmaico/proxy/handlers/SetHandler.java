package com.github.mmaico.proxy.handlers;


import com.github.mmaico.proxy.reflections.ReflectionProxyUtils;
import com.github.mmaico.shared.annotations.Attribute;
import com.github.mmaico.shared.annotations.Model;
import com.github.mmaico.shared.libraries.CollectionUtils;
import com.github.mmaico.shared.libraries.ReflectionUtils;
import com.github.mmaico.shared.libraries.registers.CollectionsImplementationRegister;
import com.github.mmaico.shared.libraries.registers.PrimitiveTypeFields;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.github.mmaico.clone.BusinessModelClone.from;
import static com.github.mmaico.shared.libraries.ReflectionUtils.invokeGetter;
import static com.github.mmaico.shared.libraries.ReflectionUtils.invokeSetter;

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
                if (CollectionUtils.isCollection(fieldObjectModel.get().getType())) {
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

                Object objectConverted = ReflectionProxyUtils.invokeConverterReverse(fieldObjectModel.get(), infoBuilder.getObjectModel());
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), objectConverted);
            } else {
                invokeSetter(infoBuilder.getHibernateEntity(), hibernateEntityField.get(), infoBuilder.getParams());

            }
        }

        return null;
    }
}
