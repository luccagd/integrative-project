package com.meli.bootcamp.integrativeproject.advice;

import com.meli.bootcamp.integrativeproject.exception.BusinessException;
import com.meli.bootcamp.integrativeproject.exception.InvalidEnumException;
import com.meli.bootcamp.integrativeproject.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Date;

@RestControllerAdvice
public class AdviceHandlerException {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<AppErrorResponse> handleNotFoundException(NotFoundException notFoundException) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;

        AppErrorResponse errorResponse = new AppErrorResponse(
                Date.from(Instant.now()),
                notFound.value(),
                notFound.name(),
                notFoundException.getMessage(),
                null);

        return new ResponseEntity<>(errorResponse, notFound);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<AppErrorResponse> handleBusinessException(BusinessException businessException) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        AppErrorResponse errorResponse = new AppErrorResponse(
                Date.from(Instant.now()),
                badRequest.value(),
                badRequest.name(),
                businessException.getMessage(),
                null);

        return new ResponseEntity<>(errorResponse, badRequest);
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handlerException(Exception exception){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }*/

    @ExceptionHandler(InvalidEnumException.class)
    public ResponseEntity<AppErrorResponse> handlerConversionFailedException(InvalidEnumException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        AppErrorResponse errorResponse = new AppErrorResponse(
                Date.from(Instant.now()),
                badRequest.value(),
                badRequest.name(),
                exception.getMessage(),
                null);

        return new ResponseEntity<>(errorResponse, badRequest);
    }

    /*
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorMessage> handlerConversionFailedException(MissingServletRequestParameterException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(exception.getMessage(), HttpStatus.BAD_REQUEST));
    }*/

}
