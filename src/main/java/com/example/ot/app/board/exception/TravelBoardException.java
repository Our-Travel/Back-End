package com.example.ot.app.board.exception;

public class TravelBoardException extends RuntimeException {
    public TravelBoardException(ErrorCode errorCode){
        super(errorCode.getMsg());
    }
}
