package com.example.ot.app.host.exception;

public class HostException extends RuntimeException {

    public HostException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }
}
