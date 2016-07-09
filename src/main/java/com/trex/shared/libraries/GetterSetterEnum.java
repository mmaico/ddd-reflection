package com.trex.shared.libraries;


public enum GetterSetterEnum {

    GETTER("get") {

        @Override
        Boolean isOperations(String methodName) {
            return methodName.startsWith(GETTER.operation);
        }
    }, SETTER("set") {
        @Override
        Boolean isOperations(String methodName) {
            return methodName.startsWith(SETTER.operation);
        }
    };

    String operation;

    GetterSetterEnum(String operation) {
        this.operation = operation;
    }

    public static Boolean isOperation(String methodName) {

        for (GetterSetterEnum item: values()) {
            if (item.isOperations(methodName)) {
                return Boolean.TRUE;
            }
        }

        return Boolean.FALSE;
    }

    public static GetterSetterEnum getOperation(String methodName) {

        for (GetterSetterEnum item: values()) {
            if (item.isOperations(methodName)) {
                return item;
            }
        }

        throw new IllegalArgumentException("Invalid method name, is not a setter or getter [" + methodName+ "]");
    }

    abstract Boolean isOperations(String methodName);

}
