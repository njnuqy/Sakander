package com.sakander.expections;

public class PersistenceException extends SakanderException{
    private static final long serialVersionUID = -7537395265357977271L;

    public PersistenceException() {
    }

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenceException(Throwable cause) {
        super(cause);
    }
}
