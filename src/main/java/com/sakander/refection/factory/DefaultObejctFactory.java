package com.sakander.refection.factory;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class DefaultObejctFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120656740914948L;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type) {
        Class<?> classToCreate = resolveInterface(type);
        return (T) instantiateClass(classToCreate);
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

    protected Class<?> resolveInterface(Class<?> type){
        Class<?> classToCreate;
        if(type == List.class || type == Collection.class || type == Iterator.class){
            classToCreate = ArrayList.class;
        }else if(type == Map.class){
            classToCreate = HashMap.class;
        }else if(type == SortedSet.class){
            classToCreate = TreeSet.class;
        }else if(type == Set.class){
            classToCreate = HashSet.class;
        }else{
            classToCreate = type;
        }
        return classToCreate;
    }
}
