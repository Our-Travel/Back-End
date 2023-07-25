package com.example.ot.app.member.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MEMBER_NOT_EXISTS("해당 회원은 존재하지 않습니다."),
    USERNAME_NOT_EXISTS("존재하지 않는 아이디입니다."),
    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다."),
    PASSWORD_LENGTH("비밀번호는 8~16자 사이어야 합니다."),
    PASSWORD_NOT_CORRECT("비밀번호는 숫자, 영문, 특수문자를 모두 포함해야 합니다."),
    PASSWORD_SAME("동일한 비밀번호로 변경할 수 없습니다."),
    NICKNAME_LENGTH("닉네임은 3~8자 사이어야 합니다."),
    NICKNAME_NOT_CORRECT("닉네임은 한글, 영문, 숫자만 가능합니다."),
    USERNAME_EXISTS("이미 존재하는 이메일입니다."),
    NICKNAME_EXISTS("이미 존재하는 닉네임입니다."),;


    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
