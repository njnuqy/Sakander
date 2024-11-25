package com.sakander.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.sakander.annotations.Id;
import com.sakander.clause.*;
import com.sakander.statement.Statement;
import com.sakander.utils.Utils;
public class SqlBuilder {
    public static String getInsertSql(Statement statement,Object object){
        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into ").append(statement.getTable().getTableName()).append(" (");
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
    public static<E> String getInsertInBatchSql(List<E> elements, Statement statement, Field[] fields){
        StringBuilder insertInBatchSql = new StringBuilder();
        insertInBatchSql.append("insert into ").append(statement.getTable().getTableName()).append(" (");
        for (Field field : fields) {
            insertInBatchSql.append(Utils.getColumnName(field)).append(",");
        }
        insertInBatchSql.deleteCharAt(insertInBatchSql.length()-1);
        insertInBatchSql.append(") values ");
        for(int i = 0 ; i < elements.size() ; i ++){
            insertInBatchSql.append("(");
            for(int j = 0 ; j < fields.length ; j++){
                insertInBatchSql.append("?,");
            }
            insertInBatchSql.deleteCharAt(insertInBatchSql.length()-1);
            insertInBatchSql.append("),");
        }
        insertInBatchSql.deleteCharAt(insertInBatchSql.length()-1);
        return insertInBatchSql.toString();
    }
    public static<E> String getUpdateSql(Statement statement,Object object){
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(statement.getTable().getTableName()).append(" set ");
        String idName = "";
        Object[] params = new Object[fields.length];
        int index = 0; // 记录参数的位置
        for(int i = 0 ; i < fields.length ; i++){
            Field field = fields[i];
            field.setAccessible(true);
            // 找到id对应的列值和名
            if(field.isAnnotationPresent(Id.class)){
                idName = field.getAnnotation(Id.class).name();
                try {
                    params[params.length - 1] = field.get(object);
                    if(params[params.length - 1] == null) {
                        throw new RuntimeException(object + "没有Id属性");
                    }
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" + object + "属性值失败");
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
        updateSql.deleteCharAt(updateSql.length() - 1);
        updateSql.append("where ").append(idName).append(" = ?");
        System.out.println(updateSql.toString());
        return updateSql.toString();
    }

    public static String getSelectSql(Statement statement){
        StringBuilder sql = new StringBuilder("select * " + " from " + statement.getTable().getTableName() + " " +
                statement.getWhere().getQuery());
        return buildSql(sql,statement).toString();
    }
    public static String getSelectWithColumnsSql(Statement statement,String ...columns){
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (String column : columns) {
            sql.append(column).append(",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" from ").append(statement.getTable().getTableName());
        sql.append(" ").append(statement.getWhere().getQuery());
        return buildSql(sql,statement).toString();
    }
    public static String getSelectWithJoinSql(Statement statement){
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(statement.getTable().getTableName()).append(" ");
        sql.append(statement.getJoin().getDirection()).append(" join ").append(statement.getJoin().getTable()).append(" on ");
        for(String on : statement.getOn().getOns()){
            sql.append(on).append(",");
        }
        sql.deleteCharAt(sql.length()-1).append(" ");
        sql.append(statement.getWhere().getQuery());
        return sql.toString();
    }
    public static String getDeleteSql(Statement statement){
        return "delete " + " from " + statement.getTable().getTableName() +
                statement.getWhere().getQuery();
    }
    public static StringBuilder buildSql(StringBuilder sql,Statement statement){
        RowRestriction rowRestriction = statement.getRowRestriction();
        GroupBy groupBy = statement.getGroupBy();
        Having having = statement.getHaving();
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