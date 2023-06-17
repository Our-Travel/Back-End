package com.example.ot.app.region.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class State {

    @Id
    private Integer id;

    private String stateName;
}
