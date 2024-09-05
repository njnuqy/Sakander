package com.sakander.model;

import com.sakander.annotations.Column;
import com.sakander.annotations.Id;
import com.sakander.annotations.Table;
import com.sakander.clause.Where;
import com.sakander.utils.Utlis;
import com.sakander.utils.JdbcUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Data
public class DbPipe<E> {
    private Statement statement;
    public DbPipe(){
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
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public void addInBatch(List<E> elements){
        elements.forEach(this::add);
    }
    public void updateByParams(E element){
        judgeIfNull(element);
        Map<String, Object> setMap = this.statement.getSet().getParams();
        Object[] setParams = setMap.values().toArray();
        Object[] params = Utlis.mergeArrays(setParams,this.statement.getWhere().getParams());
        String sql = getUpdateByParamsSql(element);
        System.out.println(sql);
        for (Object param : setParams){
            System.out.println(param);
        }
        JdbcUtils.excuteUpdate(sql, params);
    }
    public int update(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        Object[] params = new Object[fields.length];
        String sql = getUpdateSql(element,params);
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public void delete(E element){
        String sql = getDeleteSql(element);
        System.out.println("deleteSql = " + sql);
        JdbcUtils.excuteUpdate(sql, this.getStatement().getWhere().getParams());
    }
    public Object select(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        String sql = getSelectSql(element);
        return JdbcUtils.excuteSelectOne(sql, clazz, this.getStatement().getWhere().getParams());
    }
    public <T> List<T> selectInBatch(E element){
        judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        judgeIfHasFields(element,fields);
        String sql = getSelectSql(element);
        return JdbcUtils.excuteSelectInBatch(sql, clazz, this.getStatement().getWhere().getParams());
    }
    public DbPipe<E> where(String query, Object ...params){
        Where where = this.statement.getWhere();
        where.setQuery(query);
        where.setParams(params);
        return this;
    }
    public DbPipe<E> set(Map<String,Object> params){
        this.statement.getSet().setParams(params);
        return this;
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
    private String getUpdateByParamsSql(E element){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("update ").append(tableName).append(" set ");
        int index = 0;
        Map<String, Object> setParams = this.statement.getSet().getParams();
        int size = setParams.size();
        for (String key : setParams.keySet()) {
            index++;
            updateSql.append(key).append(" = ?");
            if(size != index){
                updateSql.append(",");
            }
        }
        updateSql.append(" where ").append(this.statement.getWhere().getQuery());
        System.out.println(updateSql.toString());
        return updateSql.toString();
    }
    public String getUpdateSql(E element,Object[] params){
        Class clazz = element.getClass();
        String tableName = getTableName(clazz);
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
            String columnName = getColumnName(fields[i]);
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
        if(existTableAnno){
            Table tableAnno = clazz.getAnnotation(Table.class);
            return tableAnno.name();
        }
        System.out.println(Utlis.toSnakeCase(clazz.getSimpleName()));
        return Utlis.toSnakeCase(clazz.getSimpleName());
    }
    private String getColumnName(Field field){
        boolean isPresent = field.isAnnotationPresent(Column.class);
        if(isPresent){
            return field.getAnnotation(Column.class).name();
        }
        return Utlis.toSnakeCase(field.getName());
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
