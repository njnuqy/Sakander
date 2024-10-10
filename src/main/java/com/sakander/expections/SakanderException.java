package com.sakander.expections;

public class SakanderException extends RuntimeException{
    private static final long serialVersionUID = 3880206998166270510L;

    public SakanderException(){

    }

    public SakanderException(String message){
        super(message);
    }

    public SakanderException(String message, Throwable cause){
        super(message,cause);
    }

    public SakanderException(Throwable cause){
        super(cause);
    }
}
