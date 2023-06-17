package com.example.ot.app.region.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class City {

    @Id
    private Integer id;

    private String cityName;

    @ManyToOne
    private State state;

}
