package com.sakander.cache;

public interface Cache {
    String getId();

    void putObject(Object key,Object value);

    Object getObejct(Object key);

    Object removeObejct(Object key);

    void clear();

    int getSize();
}
