package com.example.ot.app.localplaces.entity;

import com.example.ot.app.member.entity.Member;
import jakarta.persistence.ManyToOne;

public class LikedSpot {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Spot spot;

}
