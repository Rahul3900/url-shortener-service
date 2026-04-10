package com.backend.url_shortener_service.Util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Base32Encoder {

    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int base = 62;
    private static SecureRandom secureRandom = new SecureRandom();

    public String getShortCode(int len){
        StringBuilder sb = new StringBuilder();

       for(int i=0;i<len;i++){
           sb.append(BASE62.charAt(secureRandom.nextInt(base)));
       }

       return sb.toString();
    }
}
