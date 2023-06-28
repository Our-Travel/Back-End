package com.example.ot.app.member.exception;

public class MemberException extends RuntimeException {

    public MemberException(final String message) {
        super(message);
    }

    public MemberException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }
}
