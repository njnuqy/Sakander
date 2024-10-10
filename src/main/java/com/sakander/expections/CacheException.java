package com.sakander.expections;

import jakarta.persistence.PersistenceException;

public class CacheException extends PersistenceException {
    private static final long serialVersionUID = -193202262468464650L;

    public CacheException(){

    }
    public CacheException(String message){
        super(message);
    }
    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }
    public CacheException(Throwable cause) {
        super(cause);
    }
}
