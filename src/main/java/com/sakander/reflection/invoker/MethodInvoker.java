package com.sakander.reflection.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodInvoker implements Invoker{

    private final Class<?> type;
    private final Method method;

    public MethodInvoker(Method method){
        this.method = method;

        if(method.getParameterTypes().length == 1){
            type = method.getParameterTypes()[0];
        }else{
            type = method.getReturnType();
        }
    }

    @Override
    public Object invoke(Object target, Object[] args) throws IllegalAccessException, InvocationTargetException {
        try {
            return method.invoke(target,args);
        }catch (IllegalAccessException e){
            method.setAccessible(true);
            return method.invoke(target,args);
        }
    }

    @Override
    public Class<?> getType() {
        return null;
    }
}
