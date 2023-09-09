package com.example.ot.app.travelInfo.entity;


import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
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

}
