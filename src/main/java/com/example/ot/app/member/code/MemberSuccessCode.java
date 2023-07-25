package com.example.ot.app.member.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberSuccessCode implements Code {
    SIGNUP_CREATED("회원가입이 완료되었습니다."),
    EMAIL_AVAILABLE("%s는 사용가능한 이메일입니다."),
    NICKNAME_AVAILABLE("%s는 사용가능한 닉네임입니다."),

    LOGIN_COMPLETED("로그인 되었습니다."),
    MY_PAGE("마이페이지입니다."),
    PROFILE_EDIT_PAGE("프로필 편집페이지입니다."),
    PROFILE_IMAGE_UPDATED("프로필사진이 수정되었습니다."),
    PROFILE_IMAGE_DELETED("기본 프로필사진으로 적용되었습니다."),
    PASSWORD_CORRECTED("올바른 비밀번호입니다.");

    private String message;

    public String formatted(String formatValue) {
        this.message = getMessage().formatted(formatValue);
        return this.message;
    }
}
