package com.example.ot.app.base.exceptionHandler;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.util.Util;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.stream.Collectors;

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

        return Util.spring.responseEntityOf(RsData.fail(msg, data));
    }

//    @ExceptionHandler(RuntimeException.class)
//    @ResponseStatus(INTERNAL_SERVER_ERROR)
//    public RsData<String> errorHandler(RuntimeException exception) {
//        String msg = exception.getClass().toString();
//
//        String data = exception.getMessage();
//
//        return RsData.fail(msg, data);
//    }

    @ExceptionHandler(MultipartException.class)
    public  ResponseEntity<RsData> NotExistsFile() {
        return Util.spring.responseEntityOf(RsData.fail("파일을 업로드해주세요."));
    }

    @ExceptionHandler({
            MemberException.class,
            HostException.class
    })
    public ResponseEntity<RsData> handleNoSuchData(final RuntimeException e) {
        return Util.spring.responseEntityOf(RsData.fail(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RsData> handleNullPointerException(NullPointerException e) {
        RsData response = RsData.fail("로그인이 필요합니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
