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

    private Long id;
    private String placeName;
    private String phone;
    private String placeUrl;
    private String address;
    private String roadAddress;
    private double longitude;
    private double latitude;

    public static ShowMapDataResponse of(TravelInfo travelInfo) {
        return ShowMapDataResponse.builder()
                .id(travelInfo.getId())
                .placeName(travelInfo.getPlaceName())
                .phone(travelInfo.getPhone())
                .placeUrl(travelInfo.getPlaceUrl())
                .address(travelInfo.getAddress())
                .roadAddress(travelInfo.getRoadAddress())
                .longitude(travelInfo.getLongitude())
                .latitude(travelInfo.getLatitude())
                .build();
    }
}
