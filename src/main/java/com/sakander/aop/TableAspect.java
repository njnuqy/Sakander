package com.sakander.aop;

import com.sakander.condition.Condition;
import com.sakander.utils.Utils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
public class TableAspect {

    @Before("@annotation(com.sakander.annotations.TableAdd)")
    public void addTable(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {
        // 目标对象实例
        Object targetObject = joinPoint.getTarget();
        System.out.println("6666");
        Object[] args = joinPoint.getArgs();
        Field field = joinPoint.getClass().getDeclaredField("type");
        field.setAccessible(true);
        Class<?> type = (Class<?>) field.get(targetObject);
        Condition condition = (Condition) args[0];
        condition.getTable().setTableName(Utils.getTableName(type));
    }

}
