package com.example.ot.app.localplaces.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import com.example.ot.app.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LikedHotel extends BaseTimeEntity {

    @ManyToOne
    private Member member;

    @ManyToOne
    private Hotel hotel;

}
