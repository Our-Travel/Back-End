package com.example.ot.app.travelInfo.dto.response;

import com.example.ot.app.travelInfo.entity.TravelInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowMapDataResponse {

    private int contentId;
    private int contentTypeId;
    private String title;
    private String address;
    private String tel;
    private String telName;
    private double longitude;
    private double latitude;
    private String image;
    private String homePage;
    private String overView;

    public static ShowMapDataResponse of(TravelInfo travelInfo) {
        return ShowMapDataResponse.builder()
                .contentId(travelInfo.getContentId())
                .contentTypeId(travelInfo.getContentTypeId())
                .title(travelInfo.getTitle())
                .address(travelInfo.getAddress())
                .tel(travelInfo.getTel())
                .telName(travelInfo.getTelName())
                .longitude(travelInfo.getLongitude())
                .latitude(travelInfo.getLatitude())
                .image(travelInfo.getImage())
                .homePage(travelInfo.getHomePage())
                .overView(travelInfo.getOverView())
                .build();
    }
}
