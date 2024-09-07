package com.sakander.model;

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
    private E element;
    private DbPipe(){
        this.statement = new Statement();
    }
    public static <E> DbPipe<E> create(E element){
        DbPipe<E> dbPipe = new DbPipe<>();
        dbPipe.element = element;
        return dbPipe;
    }
    public int add(E element){
        Utlis.judgeIfNull(element);
        Class clazz = element.getClass();
        String tableName = Utlis.getTableName(clazz);
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(element,fields);
        String sql = SqlBuilder.getInsertSql(tableName,fields);
        Object[] params = Utlis.getSqlParams(element,fields);
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public int addInBatch(List<E> elements){
        elements.forEach(Utlis::judgeIfNull);
        Class clazz = elements.get(0).getClass();
        this.statement.getTable().setTableName(Utlis.getTableName(clazz));
        Field[] fields = clazz.getDeclaredFields();
        String sql = SqlBuilder.getInsertInBatchSql(elements,this.statement,fields);
        Object[] params = Utlis.getListParams(elements,elements.size() * fields.length);
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public int update(E element){
        Utlis.judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(element,fields);
        Object[] params = new Object[fields.length];
        String sql = SqlBuilder.getUpdateSql(element,params);
        return JdbcUtils.excuteUpdate(sql,params);
    }
    public int updateByParams(E element){
        Utlis.judgeIfNull(element);
        Map<String, Object> setMap = this.statement.getSet().getParams();
        Object[] setParams = setMap.values().toArray();
        Object[] params = Utlis.mergeArrays(setParams,this.statement.getWhere().getParams());
        String sql = SqlBuilder.getUpdateByParamsSql(element,this.statement);
        return JdbcUtils.excuteUpdate(sql, params);
    }
    public void delete(E element){
        String sql = SqlBuilder.getDeleteSql(element,this.statement);
        System.out.println("deleteSql = " + sql);
        JdbcUtils.excuteUpdate(sql, this.getStatement().getWhere().getParams());
    }
    public Object select(){
        Utlis.judgeIfNull(this.element);
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getSelectSql(this.element,this.statement);
        return JdbcUtils.excuteSelectOne(sql, clazz, this.getStatement().getWhere().getParams());
    }
    public <T> List<T> selectInBatch(E element){
        Utlis.judgeIfNull(element);
        Class clazz = element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(element,fields);
        String sql = SqlBuilder.getSelectSql(this.element,this.statement);
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

}
