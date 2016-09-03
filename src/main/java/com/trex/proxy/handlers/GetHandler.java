package com.trex.proxy.handlers;


import com.trex.proxy.ProxyCollectionHandler;
import com.trex.proxy.ProxyInterceptor;
import com.trex.proxy.reflections.ReflectionProxyUtils;
import com.trex.shared.annotations.Attribute;
import com.trex.shared.annotations.Model;
import com.trex.shared.libraries.ReflectionUtils;
import com.trex.shared.libraries.registers.PrimitiveTypeFields;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.trex.shared.libraries.CollectionUtils.isCollection;

public class GetHandler implements Handler {

    @Override
    public Object handler(HandlerInfoBuilder infoBuilder) {

        Optional<Field> fieldObjectModel = ReflectionProxyUtils.getFieldByGetOrSet(infoBuilder.getObjectModel(), infoBuilder.getMethod().getName());

        if (fieldObjectModel.isPresent() && fieldObjectModel.get().getAnnotation(Attribute.class) != null
                && !fieldObjectModel.get().getAnnotation(Attribute.class).converter().isInterface()) {
            return ReflectionProxyUtils.invokeConverter(fieldObjectModel.get(), infoBuilder.getHibernateEntity());
        }

        String methodName = infoBuilder.getMethod().getName();

        Optional<Field> hibernateEntityFieldFound = infoBuilder.getHibernateEntityField();

        if (!hibernateEntityFieldFound.isPresent()) {
            throw new IllegalArgumentException("Method name [ " + methodName +"] "
                    + "not found on [ " + infoBuilder.getHibernateEntity().getClass() + "]");
        }

        Model annotation = null;
        if (!PrimitiveTypeFields.getInstance().contains(hibernateEntityFieldFound.get().getType())) {
            annotation = infoBuilder.getObjectModel().getClass().getSuperclass().getAnnotation(Model.class);
        }

        boolean isDDDModel = annotation != null;

        if (isDDDModel) {
            Object result = ReflectionUtils.getValue(infoBuilder.getHibernateEntity(), hibernateEntityFieldFound.get());
            if (isCollection(hibernateEntityFieldFound.get().getType())) {
                return ProxyCollectionHandler
                        .createProxyCollection((Collection) result, fieldObjectModel.get()).proxy();
            } else {
                return Enhancer.create(infoBuilder.getMethod().getReturnType(), ProxyInterceptor.create(result));
            }
        } else {
            return ReflectionUtils.getValue(infoBuilder.getHibernateEntity(), hibernateEntityFieldFound.get());
        }
    }

}
