package com.dsvl.flood.exceptions;

public class ErroneousResponseException extends Exception{
    public ErroneousResponseException(String msg) {
        super(msg);
    }

    public ErroneousResponseException(Throwable throwable) {
        super(throwable);
    }

    public ErroneousResponseException(String message, Throwable e) {
        super(message, e);
    }
}
