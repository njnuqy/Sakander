package com.sakander.model;

import com.sakander.annotations.Column;
import com.sakander.annotations.Id;
import com.sakander.annotations.Table;
import com.sakander.clause.Where;
import com.sakander.utils.JdbcUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Arrays;

@Data
public class dbPipe<E> {
    private Statement statement;
    public dbPipe(){
        this.statement = new Statement();
    }
    public int add(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        String sql = getInsertSql(tableName,fields.length);
        Object[] params = getSqlParams(element,fields);
        System.out.println("insertSql = " + sql);
        System.out.println(Arrays.toString(params));
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public int update(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        Object[] params = new Object[fields.length];
        String sql = getUpdateSqlAndParams(element,params);
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public int delete(E element){
        String sql = getDeleteSql(element);
        System.out.println("deleteSql = " + sql);
        return JdbcUtils.excuteUpdate(sql,this.getStatement().getWhere().getParams());
    }
    public Object select(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        String sql = getSelectSql(element);
        return JdbcUtils.excuteSelectOne(sql, clazz, this.getStatement().getWhere().getParams());
    }
    private String getSelectSql(E element){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        this.getStatement().setTableName(tableName);
        StringBuilder selectSql = new StringBuilder();
        selectSql.append("select * from ").append(tableName).append(" where ").append(this.getStatement().getWhere().getQuery());
        return selectSql.toString();
    }
    private String getDeleteSql(E element){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        this.getStatement().setTableName(tableName);
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("delete from ").append(tableName).append(" where ").append(this.getStatement().getWhere().getQuery());
        return deleteSql.toString();
    }
    public dbPipe<E> where(String query,Object ...params){
        Where where = this.getStatement().getWhere();
        where.setQuery(query);
        where.setParams(params);
        return this;
    }
    private String getUpdateSqlAndParams(E element,Object[] params){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");
        String idName = "";
        int index = 0; // 记录参数的位置
        for(int i = 0 ; i < fields.length ; i++){
            fields[i].setAccessible(true);
            // 找到id对应的列值和名
            if(fields[i].isAnnotationPresent(Id.class)){
                    idName = fields[i].getAnnotation(Id.class).name();
                    try {
                        params[params.length - 1] = fields[i].get(element);
                        if(params[params.length - 1] == null) {
                            throw new RuntimeException(element + "没有Id属性");
                        }
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" + element + "属性值失败");
                }
            }
            boolean isPresent = fields[i].isAnnotationPresent(Column.class);
            if(isPresent){
                Column column = fields[i].getAnnotation(Column.class);
                String columnName = column.name();
                updateSql.append(" ").append(columnName).append(" = ? ,");
                try {
                    params[index++] = fields[i].get(element);
                }catch (IllegalAccessException e){
                    System.out.println(e.getMessage());
                    System.out.println("获取" +  element + "的属性值失败");
                }
            }
        }
        updateSql.deleteCharAt(updateSql.length() - 1);
        updateSql.append("where ").append(idName).append(" = ?");
        System.out.println(updateSql.toString());
        return updateSql.toString();
    }
    private Object[] getSqlParams(E element,Field[] fields){
        Object[] params = new Object[fields.length];
        for(int i = 0 ; i < fields.length ; i++){
            fields[i].setAccessible(true);
            try {
                params[i] = fields[i].get(element);
            }catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
                System.out.println("获取" + element + "的属性值失败");
                e.printStackTrace();
            }
        }
        return params;
    }
    private String getInsertSql(String tableName,int length){
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" values(");
        for(int i = 0 ; i < length ; i++){
            sql.append("?,");
        }
        sql.deleteCharAt(sql.length()-1);
        sql.append(")");
        return sql.toString();
    }
    private String getTableName(Class<E> clazz){
        boolean existTableAnno = clazz.isAnnotationPresent(Table.class);
        if(!existTableAnno){
            throw new RuntimeException(clazz + "没有Table注解");
        }
        Table tableAnno = (Table) clazz.getAnnotation(Table.class);
        return tableAnno.name();
    }
    private void judgeIfNull(E element){
        if(element == null){
            throw new IllegalArgumentException("插入的元素为空");
        }
    }
    private void judgeIfHasFields(E element,Field[] fields){
        if(fields == null || fields.length == 0){
            throw new RuntimeException(element + "没有属性");
        }
    }
}
