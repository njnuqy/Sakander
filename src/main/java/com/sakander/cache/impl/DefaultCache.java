package com.sakander.cache.impl;

import com.sakander.cache.Cache;
import com.sakander.cache.CacheException;

import java.util.HashMap;
import java.util.Map;

public class DefaultCache implements Cache {
    // TODO id的作用是什么？
    private final String id;

    private final Map<Object,Object> cache = new HashMap<>();

    public DefaultCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
    // 存放缓存数据
    @Override
    public void putObject(Object key, Object value) {
        cache.put(key,value);
    }
    // 返回缓存数据
    @Override
    public Object getObejct(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObejct(Object key) {
        return cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
    // 获得Cache的容量
    @Override
    public int getSize() {
        return cache.size();
    }
    // TODO 这是什么equals
    @Override
    public boolean equals(Object o) {
        if(getId() == null){
            throw new CacheException("Cache instances require an ID.");
        }
        if(this == o){
            return true;
        }
        if(!(o instanceof Cache)){
            return false;
        }
        Cache otherCache = (Cache)o;
        return getId().equals(otherCache.getId());
    }

    @Override
    public int hashCode() {
        if(getId() == null){
            throw new CacheException("Cache instances require an ID.");
        }
        return getId().hashCode();
    }
}
