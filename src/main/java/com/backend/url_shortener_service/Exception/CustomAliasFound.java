package com.backend.url_shortener_service.Exception;


public class CustomAliasFound extends RuntimeException{

    public CustomAliasFound(String message){
        super(message);
    }
}
