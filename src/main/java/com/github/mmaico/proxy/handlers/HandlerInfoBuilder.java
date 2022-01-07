package com.github.mmaico.proxy.handlers;


import com.github.mmaico.proxy.reflections.ReflectionProxyUtils;
import com.github.mmaico.shared.libraries.GetterSetterEnum;
import com.github.mmaico.shared.libraries.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

public class HandlerInfoBuilder {
    private final Object objectModel;
    private final Method method;
    private final Object[] params;
    private final Object hibernateEntity;
    private final Optional<Field> hibernateEntityField;

    public HandlerInfoBuilder(Object objectModel, Method method, Object[] params, Object hibernateEntity) {
        this.objectModel = objectModel;
        this.method = method;
        this.params = params;
        this.hibernateEntity = hibernateEntity;

        String fieldName = ReflectionProxyUtils.getHibernateEntityFieldNameBy(objectModel, method.getName());
        this.hibernateEntityField = ReflectionUtils.getField(hibernateEntity, fieldName);
    }


    public static HandlerInfoBuilder create(Object objectModel, Method method, Object[] params, Object hibernateEntity) {
        return new HandlerInfoBuilder(objectModel, method, params, hibernateEntity);
    }

    public Object getObjectModel() {
        return objectModel;
    }

    public Method getMethod() {
        return method;
    }

    public Object getHibernateEntity() {
        return hibernateEntity;
    }

    public Boolean isGetOrSetOperation() {
        return GetterSetterEnum.isOperation(method.getName());
    }

    public GetterSetterEnum getOperation() {
        return GetterSetterEnum.getOperation(method.getName());
    }

    public Optional<Field> getHibernateEntityField() {
        return hibernateEntityField;
    }

    public Object getParams() {
        return params[0];
    }


}
