package com.meli.bootcamp.integrativeproject.exception;

import org.springframework.http.HttpStatus;

public class InvalidEnumException extends RuntimeException {
    private HttpStatus httpStatus;

    public InvalidEnumException() {
        super("Category sent as parameter does not exist! The valid categories are CONGELADO, REFRIGERADO and FRESH");
    }
}
