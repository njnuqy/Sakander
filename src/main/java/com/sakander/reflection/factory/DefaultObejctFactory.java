package com.sakander.reflection.factory;

import com.sakander.reflection.ReflectionException;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.*;

public class DefaultObejctFactory implements ObjectFactory, Serializable {

    private static final long serialVersionUID = -8855120656740914948L;

    @Override
    public <T> T create(Class<T> type) {
        return create(type,null,null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T create(Class<T> type,List<Class<?>> constructorArgTypes,List<Object> constructorArgs) {
        Class<?> classToCreate = resolveInterface(type);
        return (T) instantiateClass(classToCreate,constructorArgTypes,constructorArgs);
    }

    private <T> T instantiateClass(Class<T> type,List<Class<?>> constructorArgTypes,List<Object> constructorArgs){
        try {
            Constructor<T> constructor;
            if (constructorArgTypes == null || constructorArgs == null) {
                constructor = type.getDeclaredConstructor();
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
            constructor = type.getDeclaredConstructor(constructorArgTypes.toArray(new Class[0]));
            constructor.setAccessible(true);
            return constructor.newInstance(constructorArgs.toArray(new Object[0]));
        }catch (Exception e){
            throw new ReflectionException(e);
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
