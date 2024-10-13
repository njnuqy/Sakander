package com.sakander.refection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;

public class DefaultObejctFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120656740914948L;

    @Override
    public <T> T create(Class<T> type) {
        return null;
    }

    private <T> T instantiateClass(Class<T> type){
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (Exception e){
            throw new RuntimeException("create instance of" + type.getName() + "failed" , e);
        }
    }
}
