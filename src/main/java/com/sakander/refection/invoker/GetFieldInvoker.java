package com.sakander.refection.invoker;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class GetFieldInvoker implements Invoker{
    private final Field field;

    public GetFieldInvoker(Field field){
        this.field = field;
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        field.setAccessible(true);
        return field.get(target);
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }
}
