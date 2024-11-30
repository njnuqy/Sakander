package com.sakander.model;

import java.lang.reflect.Field;

import com.sakander.annotations.Id;
import com.sakander.clause.*;
import com.sakander.condition.QueryCondition;
import com.sakander.condition.UpdateCondition;
import com.sakander.utils.Utils;
public class SqlBuilder {
    public static String getInsertSql(UpdateCondition condition){
        Field[] fields = condition.getInsert().getObject().getClass().getDeclaredFields();
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into ").append(condition.getTable().getTableName()).append(" (");
        for (Field field : fields) {
            insertSql.append(Utils.getColumnName(field)).append(",");
        }
        insertSql.deleteCharAt(insertSql.length()-1);
        insertSql.append(") values (");
        for(int i = 0 ; i < fields.length ; i++){
            insertSql.append("?,");
        }
        insertSql.deleteCharAt(insertSql.length()-1);
        insertSql.append(")");
        return insertSql.toString();
    }

    public static void prepareUpdate(UpdateCondition condition){
        Object object = condition.getUpdate().getObject();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(condition.getTable().getTableName()).append(" set");
        String idName = "";
        Object[] params = new Object[fields.length + 1];
        int index = 0; // 记录参数的位置
        for(int i = 0 ; i < fields.length ; i++){
            Field field = fields[i];
            field.setAccessible(true);
            //找到id对应的列值和名
            if(field.isAnnotationPresent(Id.class)){
                idName = field.getAnnotation(Id.class).name();
                try {
                    params[fields.length] = field.get(object);
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" +  object + "的属性值失败");
                }
            }
            String columnName = Utils.getColumnName(fields[i]);
            updateSql.append(" ").append(columnName).append(" = ? ,");
            try {
                params[index++] = fields[i].get(object);
            }catch (IllegalAccessException e){
                System.out.println(e.getMessage());
                System.out.println("获取" +  object + "的属性值失败");
            }
        }
        if(idName.isEmpty()){
            throw new RuntimeException(object + "没有Id属性");
        }
        condition.setParameters(params);
        updateSql.deleteCharAt(updateSql.length() - 1);
        updateSql.append("where ").append(idName).append(" = ?");
        condition.setSQL(updateSql.toString());
    }
    public static String getSelectSql(QueryCondition condition){
        StringBuilder sql = new StringBuilder("select * " + " from " + condition.getTable().getTableName() + " " +
                condition.getWhere().getQuery());
        return buildSql(sql,condition).toString();
    }
    public static void prepareDelete(UpdateCondition condition){
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from ").append(condition.getTable().getTableName()).append(" where ");
        Object object = condition.getDelete().getObject();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if(field.isAnnotationPresent(Id.class)){{
                field.setAccessible(true);
                deleteSql.append(Utils.getColumnName(field)).append(" = ?");
                try {
                    condition.setParameters(new Object[]{field.get(object)});
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" +  object + "的属性值失败");
                }
                break;
            }}
        }
        condition.setSQL(deleteSql.toString());
    }
    public static StringBuilder buildSql(StringBuilder sql,QueryCondition condition){
        RowRestriction rowRestriction = condition.getRowRestriction();
        GroupBy groupBy = condition.getGroupBy();
        Having having = condition.getHaving();
        if(groupBy.getParams() != null){
            sql.append(" group by ");
            for (Object param : groupBy.getParams()) {
                sql.append(param).append(",");
            }
            sql.deleteCharAt(sql.length()-1);
        }
        if(having.getQuery() != null){
            sql.append(" having ").append(having.getQuery());
        }
        sql.append(" limit ").append(rowRestriction.getLimit());
        sql.append(" offset ").append(rowRestriction.getOffset());
        return sql;
    }
}