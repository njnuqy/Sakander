package com.sakander.reflection;

import java.lang.reflect.*;
import java.util.Arrays;

public class TypeParameterResolver {
    public static Type resolveFieldType(Field field, Type srcType) {
        Type fieldType = field.getGenericType();
        Class<?> declaringClass = field.getDeclaringClass();
        return resolveType(fieldType, srcType, declaringClass);
    }

    public static Type resolveReturnType(Method method,Type srcType){
        Type returnType = method.getGenericReturnType();
        Class<?> declaringClass = method.getDeclaringClass();
        return resolveType(returnType,srcType,declaringClass);
    }

    private static Type resolveType(Type type, Type srcType, Class<?> declaringClass) {
        if (type instanceof TypeVariable) {
            return resolveTypeVar((TypeVariable<?>) type, srcType, declaringClass);
        }
        if (type instanceof ParameterizedType) {
            return resolveParameterizedType((ParameterizedType) type, srcType, declaringClass);
        } else if (type instanceof GenericArrayType) {
            return resolveGenericArrayType((GenericArrayType) type, srcType, declaringClass);
        } else {
            return type;
        }
    }
    private static Type resolveGenericArrayType(GenericArrayType genericArrayType, Type srcType,
                                                Class<?> declaringClass) {
        Type componentType = genericArrayType.getGenericComponentType();
        Type resolvedComponentType = null;
        if (componentType instanceof TypeVariable) {
            resolvedComponentType = resolveTypeVar((TypeVariable<?>) componentType, srcType, declaringClass);
        } else if (componentType instanceof GenericArrayType) {
            resolvedComponentType = resolveGenericArrayType((GenericArrayType) componentType, srcType, declaringClass);
        } else if (componentType instanceof ParameterizedType) {
            resolvedComponentType = resolveParameterizedType((ParameterizedType) componentType, srcType, declaringClass);
        }
        if (resolvedComponentType instanceof Class) {
            return Array.newInstance((Class<?>) resolvedComponentType, 0).getClass();
        }
        return new GenericArrayTypeImpl(resolvedComponentType);
    }
    private static ParameterizedType resolveParameterizedType(ParameterizedType parameterizedType, Type srcType,
                                                              Class<?> declaringClass) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        Type[] typeArgs = parameterizedType.getActualTypeArguments();
        Type[] args = new Type[typeArgs.length];
        for (int i = 0; i < typeArgs.length; i++) {
            if (typeArgs[i] instanceof TypeVariable) {
                args[i] = resolveTypeVar((TypeVariable<?>) typeArgs[i], srcType, declaringClass);
            } else if (typeArgs[i] instanceof ParameterizedType) {
                args[i] = resolveParameterizedType((ParameterizedType) typeArgs[i], srcType, declaringClass);
            } else if (typeArgs[i] instanceof WildcardType) {
                args[i] = resolveWildcardType((WildcardType) typeArgs[i], srcType, declaringClass);
            } else {
                args[i] = typeArgs[i];
            }
        }
        return new ParameterizedTypeImpl(rawType, null, args);
    }

    private static Type resolveWildcardType(WildcardType wildcardType, Type srcType, Class<?> declaringClass) {
        Type[] lowerBounds = resolveWildcardTypeBounds(wildcardType.getLowerBounds(), srcType, declaringClass);
        Type[] upperBounds = resolveWildcardTypeBounds(wildcardType.getUpperBounds(), srcType, declaringClass);
        return new WildcardTypeImpl(lowerBounds, upperBounds);
    }

    private static Type[] resolveWildcardTypeBounds(Type[] bounds, Type srcType, Class<?> declaringClass) {
        Type[] result = new Type[bounds.length];
        for (int i = 0; i < bounds.length; i++) {
            if (bounds[i] instanceof TypeVariable) {
                result[i] = resolveTypeVar((TypeVariable<?>) bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof ParameterizedType) {
                result[i] = resolveParameterizedType((ParameterizedType) bounds[i], srcType, declaringClass);
            } else if (bounds[i] instanceof WildcardType) {
                result[i] = resolveWildcardType((WildcardType) bounds[i], srcType, declaringClass);
            } else {
                result[i] = bounds[i];
            }
        }
        return result;
    }
    private static Type resolveTypeVar(TypeVariable<?> typeVar,Type srcType,Class<?> declaringClass){
        Type result;
        Class<?> clazz;
        if(srcType instanceof Class){
            clazz = (Class<?>) srcType;
        }else if(srcType instanceof ParameterizedType){
            ParameterizedType parameterizedType = (ParameterizedType) srcType;
            clazz = (Class<?>) parameterizedType.getRawType();
        }else{
            throw new IllegalArgumentException(
                    "the 2nd arg must be Class or ParameterizedType,but was" + srcType.getClass()
            );
        }
        if(clazz == declaringClass){
            Type[] bounds = typeVar.getBounds();
            if(bounds.length > 0){
                return bounds[0];
            }
            return Object.class;
        }
        Type[] superInterfaces = clazz.getGenericInterfaces();
        for(Type superInterface : superInterfaces){
            result = scanSuperTypes(typeVar,srcType,declaringClass,clazz,superInterface);
            if(result != null){
                return result;
            }
        }
        return Object.class;
    }

    private static Type scanSuperTypes(TypeVariable<?> typeVar,Type srcType,Class<?> declaringClass,Class<?> clazz,Type superclass){
        if(superclass instanceof ParameterizedType){
            ParameterizedType parentAsType = (ParameterizedType) superclass;
            Class<?> parentAsClass = (Class<?>)parentAsType.getRawType();
            TypeVariable<?>[] parentTypeVars = parentAsClass.getTypeParameters();
            if(srcType instanceof ParameterizedType){
                parentAsType = translateParentTypeVars((ParameterizedType) srcType,clazz,parentAsType);
            }
            if (declaringClass == parentAsClass) {
                for (int i = 0; i < parentTypeVars.length; i++) {
                    if (typeVar.equals(parentTypeVars[i])) {
                        Type actualType = parentAsType.getActualTypeArguments()[i];
                        return actualType instanceof TypeVariable<?> ? Object.class : actualType;
                    }
                }
            }
        }else if (superclass instanceof Class && declaringClass.isAssignableFrom((Class<?>) superclass)) {
            return resolveTypeVar(typeVar, superclass, declaringClass);
        }
        return null;
    }

    private static ParameterizedType translateParentTypeVars(ParameterizedType srcType,Class<?> srcClass,ParameterizedType parentType){
        Type[] parentTypeArgs = parentType.getActualTypeArguments();
        Type[] srcTypeArgs = srcType.getActualTypeArguments();
        TypeVariable<?>[] srcTypeVars = srcClass.getTypeParameters();
        Type[] newParentArgs = new Type[parentTypeArgs.length];
        boolean noChange = true;
        for (int i = 0; i < parentTypeArgs.length; i++) {
            if (parentTypeArgs[i] instanceof TypeVariable) {
                for (int j = 0; j < srcTypeVars.length; j++) {
                    if (srcTypeVars[j].equals(parentTypeArgs[i])) {
                        noChange = false;
                        newParentArgs[i] = srcTypeArgs[j];
                    }
                }
            } else {
                newParentArgs[i] = parentTypeArgs[i];
            }
        }
        return noChange ? parentType : new ParameterizedTypeImpl((Class<?>) parentType.getRawType(), null, newParentArgs);
    }
    private TypeParameterResolver() {
    }

    static class ParameterizedTypeImpl implements ParameterizedType {
        private final Class<?> rawType;

        private final Type ownerType;

        private final Type[] actualTypeArguments;

        public ParameterizedTypeImpl(Class<?> rawType, Type ownerType, Type[] actualTypeArguments) {
            this.rawType = rawType;
            this.ownerType = ownerType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getOwnerType() {
            return ownerType;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public String toString() {
            return "ParameterizedTypeImpl [rawType=" + rawType + ", ownerType=" + ownerType + ", actualTypeArguments="
                    + Arrays.toString(actualTypeArguments) + "]";
        }
    }

    static class WildcardTypeImpl implements WildcardType {
        private final Type[] lowerBounds;

        private final Type[] upperBounds;

        WildcardTypeImpl(Type[] lowerBounds, Type[] upperBounds) {
            this.lowerBounds = lowerBounds;
            this.upperBounds = upperBounds;
        }

        @Override
        public Type[] getLowerBounds() {
            return lowerBounds;
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds;
        }
    }

    static class GenericArrayTypeImpl implements GenericArrayType {
        private final Type genericComponentType;

        GenericArrayTypeImpl(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }
    }
}
