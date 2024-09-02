package com.sakander.model;

import lombok.Data;

import java.util.Map;

@Data
public class MyStatement {
    private String table;
    private Object model;
    private boolean Unscoped;
    private Object dest;
    private String[] buildClauses;
    private boolean distinct;
    private String[] selects;
    private String[] omits;
    private Map<String,Object> preloads;
    private ConnPool connPool;
    private Schema schema;
    private Context context;
    private boolean raiseErrorOnNotFound;
    private boolean skipHooks;
    private String SQL;
    private Object[] vars;
    private int curDestIndex;
    private Object[] attrs;
    private Object[] assigns;
}
