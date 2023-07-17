package com.example.ot.app.host.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NO_REGION_CODE("지역을 선택해주세요."),
    NOT_EXISTS_HOST("존재하지 않는 호스트입니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
