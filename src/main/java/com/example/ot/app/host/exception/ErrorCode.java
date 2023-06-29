package com.example.ot.app.host.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NO_REGION_CODE("지역을 선택해주세요.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
