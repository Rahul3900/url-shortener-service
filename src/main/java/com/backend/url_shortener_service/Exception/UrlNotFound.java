package com.backend.url_shortener_service.Exception;

public class UrlNotFound extends RuntimeException {

    public UrlNotFound(String message){

        super(message);
    }
}
