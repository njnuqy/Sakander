package com.sakander.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.sakander.annotations.Id;
import com.sakander.clause.*;
import com.sakander.statement.Statement;
import com.sakander.utils.Utlis;
public class SqlBuilder {
    public static String getInsertSql(String tableName,Field[] fields){
        StringBuilder insertSql = new StringBuilder();
        insertSql.append("insert into ").append(tableName).append(" (");
        for (Field field : fields) {
            insertSql.append(Utlis.getColumnName(field)).append(",");
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
            insertInBatchSql.append(Utlis.getColumnName(field)).append(",");
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
    public static<E> String getUpdateSql(E element,Object[] params){
        Class clazz = element.getClass();
        String tableName = Utlis.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");
        String idName = "";
        int index = 0; // 记录参数的位置
        for(int i = 0 ; i < fields.length ; i++){
            Field field = fields[i];
            field.setAccessible(true);
            // 找到id对应的列值和名
            if(field.isAnnotationPresent(Id.class)){
                idName = field.getAnnotation(Id.class).name();
                try {
                    params[params.length - 1] = field.get(element);
                    if(params[params.length - 1] == null) {
                        throw new RuntimeException(element + "没有Id属性");
                    }
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "属性值失败");
                }
            }
            String columnName = Utlis.getColumnName(fields[i]);
            updateSql.append(" ").append(columnName).append(" = ? ,");
            try {
                params[index++] = fields[i].get(element);
            }catch (IllegalAccessException e){
                System.out.println(e.getMessage());
                System.out.println("获取" +  element + "的属性值失败");
            }
        }
        updateSql.deleteCharAt(updateSql.length() - 1);
        updateSql.append("where ").append(idName).append(" = ?");
        System.out.println(updateSql.toString());
        return updateSql.toString();
    }
    public static<E> String getUpdateByParamsSql(E element,Statement statement){
        Class clazz = element.getClass();
        String tableName = Utlis.getTableName(clazz);
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tableName).append(" set ");
        Map<String, Object> setParams = statement.getSet().getParams();
        int index = 0;
        int size = setParams.size();
        for (String key : setParams.keySet()) {
            index++;
            sql.append(key).append(" = ?");
            if(size != index){
                sql.append(",");
            }
        }
        sql.append(" where ").append(statement.getWhere().getQuery());
        System.out.println(sql);
        return sql.toString();
    }
    public static String getEasySelectSql(Statement statement){
        StringBuilder sql = new StringBuilder();
        sql.append("select * ").append(" from ").append(statement.getTable().getTableName());
        if(!statement.getWhere().equals(Where.DEFAULT_WHERE)){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        return sql.toString();
    }
    public static String getSelectWithColumnsSql(Statement statement,String ...columns){
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (String column : columns) {
            sql.append(column).append(",");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(" from ").append(statement.getTable().getTableName());
        if(statement.getWhere().getQuery() != null){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        return buildSql(sql,statement).toString();
    }
    public static String getComplexSelectSql(Statement statement){
        StringBuilder sql = new StringBuilder();
        sql.append("select * ").append(" from ").append(statement.getTable().getTableName());
        if(statement.getWhere().getQuery() != null){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        return buildSql(sql,statement).toString();
    }
    public static<E> String getDeleteSql(E element,Statement statement){
        StringBuilder sql = new StringBuilder();
        sql.append("delete ").append(" from ").append(statement.getTable().getTableName());
        if(statement.getWhere().getQuery() != null){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        return sql.toString();
    }
    public static<E> String getCountSql(E element,Statement statement){
        StringBuilder sql = new StringBuilder();
        sql.append("select count");
        for(String count : statement.getCount().getCounts()){
            sql.append("(").append(count).append("),");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" from ").append(statement.getTable().getTableName());
        return buildSql(sql,statement).toString();
    }
    public static<E> String getAggregateSql(E element,Statement statement,String ...cols){
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (String col : cols) {
            sql.append(col).append(", ");
        }
        sql = aggregatesSql(sql,statement);
        sql.append(" from ").append(statement.getTable().getTableName());
        if(statement.getWhere().getQuery() != null){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        System.out.println(buildSql(sql, statement));
        return sql.toString();
    }
    public static<E> String getJoinSql(E element,Statement statement,String ...cols){
        StringBuilder sql = new StringBuilder();
        sql.append("select ");
        for (String col : cols) {
            sql.append("t1.").append(col).append(",");
        }
        Join join = statement.getJoin();
        for (String col : join.getCols()) {
            sql.append("t2.").append(col).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);

        sql.append(" from ").append(statement.getTable().getTableName()).append(" t1 ").append(join.getDirection()).append(" join ")
                .append(join.getTable()).append(" t2 on ").append(statement.getOn().getQuery());
        if(statement.getWhere().getQuery() != null){
            sql.append(" where ").append(statement.getWhere().getQuery());
        }
        return sql.toString();
    }
    public static StringBuilder aggregatesSql(StringBuilder sql,Statement statement){
        String[] counts = statement.getCount().getCounts();
        String[] sums = statement.getSum().getSums();
        String[] maxes = statement.getMax().getMaxes();
        String[] mines = statement.getMin().getMines();
        String[] averages = statement.getAverage().getAverages();
        sql = aggregateBuilder(sql,"count",counts);
        sql = aggregateBuilder(sql,"sum",sums);
        sql = aggregateBuilder(sql,"max",maxes);
        sql = aggregateBuilder(sql,"min",mines);
        sql = aggregateBuilder(sql,"avg",averages);
        sql.deleteCharAt(sql.length() - 1);
        return sql;
    }
    public static StringBuilder aggregateBuilder(StringBuilder sql,String type,String ...params){
        if(params != null){
            for (String param : params) {
                sql.append(type).append("(").append(param).append("),");
            }
        }
        return sql;
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