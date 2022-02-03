package com.meli.bootcamp.integrativeproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private HttpStatus httpStatus = null;

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, HttpStatus httpStatus) {
        this(message);
        this.httpStatus = httpStatus;
    }
}
