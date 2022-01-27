package com.meli.bootcamp.integrativeproject.advice;


import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
public class ErrorMessage {
    private String message;
    private Integer code;
    private LocalDateTime timestamp;
    private HttpStatus httpStatus = null;

    public ErrorMessage(String message, HttpStatus httpStatus){
        this.message = message;
        this.code = httpStatus.value();
        this.httpStatus = httpStatus ;
        this.timestamp = LocalDateTime.now();
    }
}
