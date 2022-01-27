package com.meli.bootcamp.integrativeproject.advice;

import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AdviceHandlerException {

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerNotFoundException(NotFoundException notFoundException) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorMessage(notFoundException.getMessage(), httpStatus), httpStatus);
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<ErrorMessage> handlerBusinessException(BusinessException businessException) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorMessage(businessException.getMessage(), httpStatus), httpStatus);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMessage> handlerException(Exception exception){
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(new ErrorMessage(exception.getMessage(), httpStatus), httpStatus);
    }
}
