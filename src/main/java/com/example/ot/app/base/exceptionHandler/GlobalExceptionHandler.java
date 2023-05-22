package com.example.ot.app.base.exceptionHandler;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.util.Util;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice(annotations = {RestController.class})
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData> errorHandler(MethodArgumentNotValidException exception) {
        String msg = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("/"));

        String data = exception
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getCode)
                .collect(Collectors.joining("/"));

        return Util.spring.responseEntityOf(RsData.of("F-MethodArgumentNotValidException", msg, data));
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public RsData<String> errorHandler(RuntimeException exception) {
        String msg = exception.getClass().toString();

        String data = exception.getMessage();

        return RsData.of("F-RuntimeException", msg, data);
    }
}
