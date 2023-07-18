package com.example.ot.app.host.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HostSuccessCode implements Code {

    HOST_REGISTER("Host 등록이 완료되었습니다."),
    HOST_EDIT_PAGE("Host 수정 페이지로 이동합니다."),
    HOST_INFO_UPDATED("Host 정보가 수정되었습니다."),
    HOST_DELETED("Host 권한을 삭제하였습니다.");

    public String formatted(String formatValue){
        this.message = getMessage().formatted(formatValue);
        return formatValue;
    }

    private String message;
}
