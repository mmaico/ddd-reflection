package com.trex.proxy.handlers;


import com.trex.shared.libraries.GetterSetterEnum;

import java.lang.reflect.Method;

public class HandlerInfoBuilder {
    private final Object objectModel;
    private final Method method;
    private final Object[] params;
    private final Object hibernateEntity;

    public HandlerInfoBuilder(Object objectModel, Method method, Object[] params, Object hibernateEntity) {
        this.objectModel = objectModel;
        this.method = method;
        this.params = params;
        this.hibernateEntity = hibernateEntity;
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
}
