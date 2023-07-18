package com.example.ot.app.member.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MEMBER_NOT_EXISTS("해당 회원은 존재하지 않습니다."),
    USERNAME_NOT_EXISTS("존재하지 않는 아이디입니다."),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    USERNAME_EXISTS("이미 존재하는 이메일입니다."),
    NICKNAME_EXISTS("이미 존재하는 닉네임입니다."),;


    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
