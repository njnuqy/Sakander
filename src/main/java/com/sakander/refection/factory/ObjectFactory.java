package com.sakander.refection.factory;

public interface ObjectFactory {
    <T> T create(Class<T> type);
}
