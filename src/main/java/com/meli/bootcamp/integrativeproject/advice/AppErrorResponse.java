package com.meli.bootcamp.integrativeproject.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
public class AppErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;
    private int code;
    private String status;
    private String message;
    private Object data;

    public AppErrorResponse(Date timestamp, int code, String status, String message, Object data) {
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
