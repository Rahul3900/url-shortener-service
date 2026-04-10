package com.backend.url_shortener_service.Exception;

public class LimitExceedException extends RuntimeException{

    public LimitExceedException(String message){
        super(message);
    }
}
