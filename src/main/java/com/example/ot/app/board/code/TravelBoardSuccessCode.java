package com.example.ot.app.board.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TravelBoardSuccessCode implements Code {
    BOARD_CREATED("게시판이 생성되었습니다."),
    BOARD_FOUND("게시판을 조회했습니다."),
    BOARD_LIKED("'좋아요'를 눌렀습니다."),
    BOARD_LIKED_CANCELED("'좋아요'를 취소했습니다."),
    BOARD_EDIT_PAGE("게시판 수정 페이지를 조회했습니다.");

    private String message;
}
