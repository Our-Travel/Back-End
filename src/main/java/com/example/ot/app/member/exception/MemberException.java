package com.example.ot.app.member.exception;

public class MemberException extends RuntimeException {

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }
}
