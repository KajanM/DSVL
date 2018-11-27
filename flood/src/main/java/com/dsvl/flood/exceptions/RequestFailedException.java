package com.dsvl.flood.exceptions;

public class RequestFailedException extends Exception{
    public RequestFailedException(String msg) {
        super(msg);
    }

    public RequestFailedException(Throwable throwable) {
        super(throwable);
    }

    public RequestFailedException(String message, Throwable e) {
        super(message, e);
    }
}
