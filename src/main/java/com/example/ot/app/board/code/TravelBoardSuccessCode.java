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
    BOARD_EDIT_PAGE("게시판 수정 페이지를 조회했습니다."),
    BOARD_UPDATED("게시판이 수정되었습니다."),
    BOARD_DELETED("게시판이 삭제되었습니다."),
    BOARD_LIST_BY_MEMBER("작성한 게시글들 불러옵니다."),
    BOARD_LIST("게시글들 불러옵니다."),
    RECRUITMENT_CLOSED("모집을 마감했습니다."),
    BOARD_LIKED_LIST("게시판 좋아요 리스트를 불러옵니다.");

    private String message;
}
