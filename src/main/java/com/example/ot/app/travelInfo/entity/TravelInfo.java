package com.example.ot.app.travelInfo.entity;


import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class TravelInfo extends BaseTimeEntity {

    private Integer contentId;
    private Integer contentTypeId;
    private String title;
    private String address;
    private String tel;
    private String telName;
    private double longitude;
    private double latitude;
    private String image;

    @Column(columnDefinition = "TEXT")
    private String homePage;

    @Column(columnDefinition = "TEXT")
    private String overView;

}
