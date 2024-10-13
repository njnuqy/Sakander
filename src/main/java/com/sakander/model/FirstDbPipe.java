package com.sakander.model;

import com.sakander.clause.*;
import com.sakander.statement.Statement;
import com.sakander.utils.Utlis;
import com.sakander.utils.JdbcUtils;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Data
public class FirstDbPipe<E> {
    private Statement statement;
    private E element;
    private FirstDbPipe(){
        this.statement = new Statement();
    }
    public static <E> FirstDbPipe<E> create(E element){
        FirstDbPipe<E> firstDbPipe = new FirstDbPipe<>();
        firstDbPipe.element = element;
        firstDbPipe.statement.getTable().setTableName(Utlis.getTableName(element.getClass()));
        return firstDbPipe;
    }
    // sql
    public <T> List<T> querySQL(String sql){
        Class clazz = this.element.getClass();
        return JdbcUtils.executeSelectInBatch(sql,clazz);
    }
    public int updateSQL(String sql){
        return JdbcUtils.excuteUpdate(sql);
    }
    // add sql
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
    // update sql
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
        return JdbcUtils.excuteUpdate(sql,params);
    }
    // delete sql
    public void delete(){
        String sql = SqlBuilder.getDeleteSql(this.element,this.statement);
        System.out.println("deleteSql = " + sql);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        for (Object param : params) {
            System.out.println(param);
        }
        JdbcUtils.excuteUpdate(sql,params);
    }

    // select sql
    public Object select(){
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getEasySelectSql(this.statement);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        return JdbcUtils.executeSelectOne(sql,clazz, params);
    }
    public <T> List<T> easySelectInBatch(){
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getEasySelectSql(this.statement);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        return JdbcUtils.executeSelectInBatch(sql,clazz, params);
    }
    public <T> List<T> complexSelectInBatch(){
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getComplexSelectSql(this.statement);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        return JdbcUtils.executeSelectInBatch(sql,clazz, params);
    }
    public List<Map<String,Object>> selectWithColumns(String ...columns){
        Field[] fields = this.element.getClass().getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getSelectWithColumnsSql(this.statement,columns);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        return JdbcUtils.executeSelectWithColumns(sql,columns, params);
    }
    public List<Map<String, Object>> selectWithAggregate(String ...cols) {
        Utlis.judgeIfNull(this.element);
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getAggregateSql(this.element,this.statement,cols);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        return JdbcUtils.executeAggregate(sql,params);
    }
    public List<Map<String, Object>> selectWithJoin(String ...cols){
        Utlis.judgeIfNull(this.element);
        Class clazz = this.element.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Utlis.judgeIfHasFields(this.element,fields);
        String sql = SqlBuilder.getJoinSql(this.element,this.statement,cols);
        Object[] params = Utlis.mergeArrays(this.statement.getWhere().getParams());
        System.out.println(sql);
        return JdbcUtils.executeAggregate(sql,params);
    }
    // condition
    public FirstDbPipe<E> where(String query, Object ...params){
        Where where = this.statement.getWhere();
        where.setQuery(query);
        where.setParams(params);
        return this;
    }
    public FirstDbPipe<E> set(Map<String,Object> params){
        this.statement.getSet().setParams(params);
        return this;
    }
    public FirstDbPipe<E> limit(int limit, int offset){
        RowRestriction rowRestriction = new RowRestriction(limit,offset);
        this.statement.setRowRestriction(rowRestriction);
        return this;
    }
    public FirstDbPipe<E> limit(int limit){
        RowRestriction rowRestriction = new RowRestriction(limit);
        this.statement.setRowRestriction(rowRestriction);
        return this;
    }
    public FirstDbPipe<E> group(Object ...params){
        this.statement.getGroupBy().setParams(params);
        return this;
    }
    public FirstDbPipe<E> having(String query, Object ...params){
        this.statement.getHaving().setQuery(query);
        this.statement.getHaving().setParams(params);
        return this;
    }
    public FirstDbPipe<E> count(String ...counts){
        this.statement.getCount().setCounts(counts);
        return this;
    }
    public FirstDbPipe<E> sum(String ...sums){
        this.statement.getSum().setSums(sums);
        return this;
    }
    public FirstDbPipe<E> max(String ...maxes){
        this.statement.getMax().setMaxes(maxes);
        return this;
    }
    public FirstDbPipe<E> min(String ...mines){
        this.statement.getMin().setMines(mines);
        return this;
    }
    public FirstDbPipe<E> average(String ...averages){
        this.statement.getAverage().setAverages(averages);
        return this;
    }
    public FirstDbPipe<E> join(String direction, String table, String ...cols){
        Join join = this.statement.getJoin();
        join.setDirection(direction);
        join.setTable(table);
        join.setCols(cols);
        return this;
    }
    public FirstDbPipe<E> on(String query){
        On on = this.statement.getOn();
        on.setQuery(query);
        return this;
    }
}
