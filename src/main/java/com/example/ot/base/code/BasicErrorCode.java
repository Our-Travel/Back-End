package com.example.ot.base.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BasicErrorCode implements Code {
    UNAUTHORIZED("접근 권한이 없습니다."),
    FILE_NOT_EXISTS("파일을 업로드해주세요.");

    public String message;
}
