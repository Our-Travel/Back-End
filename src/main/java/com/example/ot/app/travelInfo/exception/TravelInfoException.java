package com.example.ot.app.travelInfo.exception;

public class TravelInfoException extends RuntimeException {

    public TravelInfoException(ErrorCode errorCode){
        super(errorCode.getMsg());
    }
}
