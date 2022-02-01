package com.meli.bootcamp.integrativeproject.advice;

import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdviceHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handlerNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(notFoundException.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorMessage> handlerBusinessException(BusinessException businessException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(businessException.getMessage(), HttpStatus.BAD_REQUEST));
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handlerException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }*/

    @ExceptionHandler(InvalidEnumException.class)
    public ResponseEntity<ErrorMessage> handlerConversionFailedException(InvalidEnumException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handlerConversionFailedException(MissingServletRequestParameterException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }

}
