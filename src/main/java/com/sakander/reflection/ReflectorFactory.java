package com.sakander.reflection;

public interface ReflectorFactory {
    Reflector findForClass(Class<?> type);
}
