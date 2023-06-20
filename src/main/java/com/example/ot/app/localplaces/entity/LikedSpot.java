package com.example.ot.app.localplaces.entity;

import com.example.ot.app.member.entity.Member;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;

public class LikedSpot {

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Spot spot;

}
