package com.sakander.reflection;

public class DefaultReflectorFactory implements ReflectorFactory{

    @Override
    public Reflector findForClass(Class<?> type) {
        return new Reflector(type);
    }
}
