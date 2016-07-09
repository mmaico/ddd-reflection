package com.trex.proxy.handlers;


import com.trex.proxy.ProxyCollectionHandler;
import com.trex.proxy.ProxyInterceptor;
import com.trex.proxy.reflections.ReflectionProxyUtils;
import com.trex.shared.annotations.EntityReference;
import com.trex.shared.annotations.Extractor;
import com.trex.shared.libraries.ReflectionUtils;
import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Optional;

import static com.trex.proxy.reflections.ReflectionProxyUtils.invokeCustomConverter;
import static com.trex.shared.libraries.CollectionUtils.isCollection;

public class GetHandler implements Handler {

    @Override
    public Object handler(HandlerInfoBuilder infoBuilder) {

        if (infoBuilder.getMethod().getAnnotation(Extractor.class) != null) {
            return ReflectionProxyUtils.invokeExtractor(infoBuilder.getMethod(), infoBuilder.getHibernateEntity());
        }

        String methodName = infoBuilder.getMethod().getName();

        String hibernateEntityField = ReflectionProxyUtils.getHibernateEntityFieldNameBy(infoBuilder.getObjectModel(), methodName);
        Optional<Field> hibernateEntityFieldFound = ReflectionUtils.getField(infoBuilder.getHibernateEntity(), hibernateEntityField);

        if (!hibernateEntityFieldFound.isPresent()) {
            throw new IllegalArgumentException("Attribute name [ " + hibernateEntityField +"] "
                    + "not found on [ " + infoBuilder.getHibernateEntity().getClass() + "]");
        }

        Field fieldObjectModel = ReflectionProxyUtils.getFieldByMethodName(infoBuilder.getObjectModel(), methodName);
        boolean isDDDModel = fieldObjectModel.getAnnotation(EntityReference.class) != null;

        if (isDDDModel) {
            Object result = ReflectionUtils.getValue(infoBuilder.getHibernateEntity(), hibernateEntityFieldFound.get());
            if (isCollection(hibernateEntityFieldFound.get().getType())) {
                return ProxyCollectionHandler
                        .createProxyCollection((Collection) result, fieldObjectModel).proxy();
            } else {
                return Enhancer.create(infoBuilder.getMethod().getReturnType(), ProxyInterceptor.create(result));
            }
        } else {
            if (ReflectionProxyUtils.hasCustomConverter(infoBuilder.getObjectModel(), methodName)) {
                return invokeCustomConverter(infoBuilder.getHibernateEntity(), hibernateEntityFieldFound.get(), fieldObjectModel);
            } else {
                return ReflectionUtils.getValue(infoBuilder.getHibernateEntity(), hibernateEntityFieldFound.get());
            }
        }
    }
}
