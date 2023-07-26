package com.example.ot.app.board.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    RECRUITMENT_PERIOD_INVALID("모집기간 종료일이 시작일보다 빠르게 설정할 수 없습니다."),
    TRAVEL_PERIOD_BEFORE_RECRUITMENT("모집기간보다 여행기간이 빠르게 설정할 수 없습니다."),
    TRAVEL_PERIOD_INVALID("여행기간 종료일이 시작일보다 빠르게 설정할 수 없습니다."),
    BOARD_NOT_EXISTS("존재하지 않는 게시판입니다.");

    ErrorCode(String msg){
        this.msg = msg;
    }

    private String msg;
}
