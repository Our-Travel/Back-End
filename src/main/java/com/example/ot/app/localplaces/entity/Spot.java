package com.example.ot.app.localplaces.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Spot {

    @Id
    private Long id;

    private String placeName;

    private String phone;

    private String addressName;

    private String roadAddressName;

    private double longitude;

    private double latitude;

    private String placeUrl;

}
