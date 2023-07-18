package com.example.ot.app.host.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    HOST_NOT_EXISTS("존재하지 않는 호스트입니다."),
    HOST_NOT_EXISTS_BY_REGION("해당 지역에는 호스트가 없습니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
