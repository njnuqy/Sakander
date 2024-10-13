package com.sakander.refection;

public interface ReflectorFactory {
    Reflector findForClass(Class<?> type);
}
