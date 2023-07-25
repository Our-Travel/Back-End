package com.example.ot.app.board.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TravelBoardSuccessCode implements Code {
    BOARD_CREATED("게시판이 생성되었습니다.");

    private String message;
}
