package com.example.ot.base.exceptionHandler;

import com.example.ot.app.board.exception.TravelBoardException;
import com.example.ot.app.chat.exception.ChatException;
import com.example.ot.app.host.exception.HostException;
import com.example.ot.app.member.exception.MemberException;
import com.example.ot.app.travelInfo.exception.TravelInfoException;
import com.example.ot.base.rsData.RsData;
import com.example.ot.base.s3.exception.ProfileUploadException;
import com.example.ot.util.Util;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.ot.base.code.BasicErrorCode.FILE_NOT_EXISTS;
import static com.example.ot.base.code.BasicErrorCode.UNAUTHORIZED;

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

    @ExceptionHandler(MultipartException.class)
    public  ResponseEntity<RsData> NotExistsFile() {
        return Util.spring.responseEntityOf(RsData.fail(FILE_NOT_EXISTS));
    }

    @ExceptionHandler(ProfileUploadException.class)
    public  ResponseEntity<RsData> NoSuchElementException(final NoSuchElementException e) {
        return Util.spring.responseEntityOf(RsData.fail(e.getMessage()));
    }

    @ExceptionHandler({
            MemberException.class,
            HostException.class,
            TravelBoardException.class,
            ChatException.class,
            TravelInfoException.class
    })
    public ResponseEntity<RsData> handleNoSuchData(final RuntimeException e) {
        return Util.spring.responseEntityOf(RsData.fail(e.getMessage()));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<RsData> handleNullPointerException(NullPointerException e) {
        RsData response = RsData.fail("로그인이 필요합니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<RsData> handleAccessDeniedException(AccessDeniedException e) {
        RsData rsData = RsData.fail(UNAUTHORIZED);
        return new ResponseEntity<>(rsData, HttpStatus.FORBIDDEN);
    }
}
