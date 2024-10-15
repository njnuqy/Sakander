package com.sakander.executor.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> {
    private final Type rawType;
    public TypeReference() {
        rawType = getSuperClassTypeParameter(getClass());
    }

    Type getSuperClassTypeParameter(Class<?> clazz){
        Type genericSuperClass = clazz.getGenericSuperclass();
        if(genericSuperClass instanceof Class){
            if(TypeReference.class != genericSuperClass){
                return getSuperClassTypeParameter(clazz.getSuperclass());
            }
            throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. "
                    + "Remove the extension or add a type parameter to it.");
        }

        Type rawType = ((ParameterizedType) genericSuperClass).getActualTypeArguments()[0];

        if(rawType instanceof ParameterizedType){
            rawType = ((ParameterizedType) rawType).getRawType();
        }

        return rawType;
    }

    public final Type getRawType(){
        return rawType;
    }
}
