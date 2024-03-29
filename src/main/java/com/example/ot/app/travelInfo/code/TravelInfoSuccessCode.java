package com.example.ot.app.travelInfo.code;

import com.example.ot.base.code.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TravelInfoSuccessCode implements Code {

    MAP_DATA_FOUND("모든 지도 데이터를 불러옵니다."),
    ONE_MAP_DATA_FOUND("모든 지도 데이터를 불러옵니다."),
    TRAVEL_INFO_LIKED("'좋아요'를 눌렀습니다."),
    TRAVEL_INFO_LIKED_CANCELED("'좋아요'를 취소했습니다."),
    TRAVEL_INFO_LIKED_LIST("관광지 좋아요 리스트를 불러옵니다.");

    private String message;
}
