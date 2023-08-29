package com.example.ot.app.travelInfo.entity;

import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TravelInfo extends BaseTimeEntity {
    private Long placeId;
    private String placeName;
    private String phone;
    private String placeUrl;
    private String address;
    private double longitude;
    private double latitude;

    @Enumerated(EnumType.STRING)
    private PlaceResource placeResource;
}
