package com.sakander.refection;

import com.sakander.refection.invoker.AmbiguousMethodInvoker;
import com.sakander.refection.invoker.Invoker;
import com.sakander.refection.invoker.MethodInvoker;
import com.sakander.refection.invoker.SetFieldInvoker;
import com.sakander.refection.property.PropertyNamer;
import com.sakander.utils.MapUtil;

import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;

public class Reflector {
    private final Class<?> type;
    private final String[] readablePropertyNames;
    private final String[] writablePropertyNames;
    private final Map<String,Invoker> setMethods = new HashMap<>();
    private final Map<String,Invoker> getMethods = new HashMap<>();
    private final Map<String,Class<?>> setTypes = new HashMap<>();
    private final Map<String,Class<?>> getTypes = new HashMap<>();
    private Constructor<?> defaultConstructor;

    private final Map<String,String> caseInsensitivePropertyMap = new HashMap<>();
    public Reflector(Class<?> clazz){
        type = clazz;
        addDefaultConstructor(clazz);
        Method[] classMethods = getClassMethods(clazz);
        addGetMethods(classMethods);
        addSetMethods(classMethods);
        addFields(clazz);
        readablePropertyNames = getMethods.keySet().toArray(new String[0]);
        writablePropertyNames = setMethods.keySet().toArray(new String[0]);
        for(String propName : readablePropertyNames){
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH),propName);
        }
        for(String propName : writablePropertyNames){
            caseInsensitivePropertyMap.put(propName.toUpperCase(Locale.ENGLISH),propName);
        }
    }

    // 添加默认构造器
    private void addDefaultConstructor(Class<?> clazz){
        Constructor<?> []constructors = clazz.getDeclaredConstructors();
        Arrays.stream(constructors).filter(constructor -> constructor.getParameterTypes().length == 0).findAny().
                ifPresent(constructor -> this.defaultConstructor = constructor);
    }

    private void addGetMethods(Method[] methods){
        Map<String, List<Method>> conflictingGetters = new HashMap<>();
        Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 0 && PropertyNamer.isGetter(m.getName()))
                .forEach(m -> addMethodConflict(conflictingGetters,PropertyNamer.methodToProperty(m.getName()),m));
        resolveGetterConflicts(conflictingGetters);
    }
    private void addSetMethods(Method[] methods){
        Map<String,List<Method>> conflictingSetters = new HashMap<>();
        Arrays.stream(methods).filter(m -> m.getParameterTypes().length == 1 && PropertyNamer.isSetter(m.getName()))
                .forEach(m -> addMethodConflict(conflictingSetters,PropertyNamer.methodToProperty(m.getName()),m));
    }

    private void resolveGetterConflicts(Map<String,List<Method>> conflictingGetters){
        for (Map.Entry<String, List<Method>> entry : conflictingGetters.entrySet()) {
            Method winner = null;
            String propName = entry.getKey();
            boolean isAmbiguous = false;
            for(Method candidate : entry.getValue()){
                if(winner == null){
                    winner = candidate;
                    continue;
                }
                Class<?> winnerType = winner.getReturnType();
                Class<?> candidateType = candidate.getReturnType();
                if(candidateType.equals(winnerType)){
                    if(!boolean.class.equals(candidateType)){
                        isAmbiguous = true;
                        break;
                    }
                    if(candidate.getName().startsWith("is")){
                        winner = candidate;
                    }
                }else if(candidateType.isAssignableFrom(winnerType)){
                    // OK
                }else if(winnerType.isAssignableFrom(candidateType)){
                    winner = candidate;
                }else{
                    isAmbiguous = true;
                    break;
                }
            }
            addGetMethod(propName,winner,isAmbiguous);
        }
    }

    private void addGetMethod(String name,Method method,boolean isAmbiguous){
        MethodInvoker invoker = isAmbiguous ? new AmbiguousMethodInvoker(method, MessageFormat.format(
                "Illegal overloaded getter method with ambiguous type for property ''{0}'' in class ''{1}''. This breaks the JavaBeans specification and can cause unpredictable results.",
                name, method.getDeclaringClass().getName())) : new MethodInvoker(method);
        getMethods.put(name,invoker);
        // 可以得到对应的mapper返回的类型
        Type returnType = TypeParameterResolver.resolveReturnType(method,type);
        getTypes.put(name,typeToClass(returnType));
    }

    private void addMethodConflict(Map<String,List<Method>> conflictingMethods,String name,Method method){
        if(isValidPropertyName(name)){
            List<Method> list = MapUtil.computeIfAbsent(conflictingMethods,name,k -> new ArrayList<>());
            list.add(method);
        }
    }

    private Class<?> typeToClass(Type src) {
        Class<?> result = null;
        if (src instanceof Class) {
            result = (Class<?>) src;
            // ParameterizedType 例如List<String> 会返回List.class
        } else if (src instanceof ParameterizedType) {
            result = (Class<?>) ((ParameterizedType) src).getRawType();
            // GenericArrayType 如T[]，获取数组的元素类型
        } else if (src instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) src).getGenericComponentType();
            if (componentType instanceof Class) {
                result = Array.newInstance((Class<?>) componentType, 0).getClass();
            } else {
                Class<?> componentClass = typeToClass(componentType);
                result = Array.newInstance(componentClass, 0).getClass();
            }
        }
        // 否则返回Object.class
        if (result == null) {
            result = Object.class;
        }
        return result;
    }

    private boolean isValidPropertyName(String name){
        return (!name.startsWith("$") && !"serialVersionUID".equals(name) && !"class".equals(name));
    }
    private Method[] getClassMethods(Class<?> clazz){
        Map<String,Method> uniqueMethods = new HashMap<>();
        Class<?> currentClass = clazz;
        // 得到自己类和所有父类的不重复方法
        while(currentClass != null && currentClass != Object.class){
            addUniqueMethods(uniqueMethods,currentClass.getDeclaredMethods());
            // 也需要查看接口，因为这个类可能是抽象类
            Class<?>[] interfaces = currentClass.getInterfaces();
            for(Class<?> anInterface : interfaces){
                addUniqueMethods(uniqueMethods,anInterface.getMethods());
            }
            currentClass = currentClass.getSuperclass();
        }
        Collection<Method> methods = uniqueMethods.values();
        return methods.toArray(new Method[0]);
    }

    private void addUniqueMethods(Map<String,Method> uniqueMethods,Method[] methods){
        for(Method currentMethod : methods){
            // 如果不是桥接方法
            if(!currentMethod.isBridge()){
                String signature = getSignature(currentMethod);
                if(!uniqueMethods.containsKey(signature)){
                    uniqueMethods.put(signature,currentMethod);
                }
            }
        }
    }

    private void addFields(Class<?> clazz){
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(!setMethods.containsKey(field.getName())){
                int modifiers = field.getModifiers();
                if((!Modifier.isFinal(modifiers)) || !Modifier.isStatic(modifiers)){
                    addSetField(field);
                }
            }
        }
    }

    private void addSetField(Field field){
        if(isValidPropertyName(field.getName())){
            setMethods.put(field.getName(),new SetFieldInvoker(field));
            Type fieldType = TypeParameterResolver.resolveFieldType(field,type);
            setTypes.put(field.getName(),typeToClass(fieldType));
        }
    }


    private String getSignature(Method method){
        StringBuilder sb = new StringBuilder();
        Class<?> returnType = method.getReturnType();
        sb.append(returnType.getName()).append('#');
        sb.append(method.getName());
        Class<?> []parameters = method.getParameterTypes();
        for(int i = 0 ; i < parameters.length ; i++){
            sb.append(i == 0 ? ':' : ',').append(parameters[i].getName());
        }
        return sb.toString();
    }

}
