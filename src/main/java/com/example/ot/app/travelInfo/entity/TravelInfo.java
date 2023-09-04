package com.example.ot.app.travelInfo.entity;

import com.example.ot.base.api.dto.DocumentDTO;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
@Getter
@Entity
public class TravelInfo extends BaseTimeEntity {
    private Long placeId;
    private String placeName;
    private String phone;
    private String placeUrl;
    private String address;
    private String roadAddress;
    private double longitude;
    private double latitude;

    @Enumerated(EnumType.STRING)
    private PlaceResource placeResource;

    public static TravelInfo of(DocumentDTO documentDTO) {
        return TravelInfo.builder()
                .placeId(documentDTO.getId())
                .placeName(documentDTO.getPlaceName())
                .phone(documentDTO.getPhone())
                .placeUrl(documentDTO.getPlaceUrl())
                .address(documentDTO.getAddressName())
                .roadAddress(documentDTO.getRoadAddressName())
                .longitude(documentDTO.getLongitude())
                .latitude(documentDTO.getLatitude())
                .build();
    }
}
