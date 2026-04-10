package com.backend.url_shortener_service.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UrlNotFound.class)
    public ResponseEntity<Map> urlNotFound(UrlNotFound ex, HttpServletRequest httpServletRequest){
        Map<String,Object> map = new HashMap<>();

        map.put("timestamp: ", LocalDateTime.now());
        map.put("status: ", HttpStatus.NOT_FOUND.value());
        map.put("error: ", ex.getMessage());
        map.put("path: ",httpServletRequest.getRequestURI());

        return new ResponseEntity<>(map,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomAliasFound.class)
    public ResponseEntity<Map> aliasFound(CustomAliasFound ex, HttpServletRequest httpServletRequest){
        Map<String,Object> map = new HashMap<>();

        map.put("timestamp: ", LocalDateTime.now());
        map.put("status: ", HttpStatus.CONFLICT.value());
        map.put("error: ", ex.getMessage());
        map.put("path: ",httpServletRequest.getRequestURI());

        return new ResponseEntity<>(map,HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LimitExceedException.class)
    public ResponseEntity<Map> limitExceed(LimitExceedException ex, HttpServletRequest httpServletRequest){
        Map<String,Object> map = new HashMap<>();

        map.put("timestamp: ", LocalDateTime.now());
        map.put("status: ", HttpStatus.BANDWIDTH_LIMIT_EXCEEDED.value());
        map.put("error: ", ex.getMessage());
        map.put("path: ",httpServletRequest.getRequestURI());

        return new ResponseEntity<>(map,HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map> methodNotValid(MethodArgumentNotValidException ex, HttpServletRequest httpServletRequest){
        Map<String,Object> map = new HashMap<>();

        map.put("timestamp: ", LocalDateTime.now());
        map.put("status: ", HttpStatus.BAD_REQUEST.value());
        map.put("error: ", ex.getBindingResult().getFieldError().getDefaultMessage());
        map.put("path: ",httpServletRequest.getRequestURI());

        log.error("Validation error: {}", ex.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<>(map,HttpStatus.BAD_REQUEST);
    }
}
