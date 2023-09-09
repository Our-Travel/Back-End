package com.example.ot.app.travelInfo.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TRAVEL_INFO_NOT_EXISTS("관광지 정보가 존재하지 않습니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
