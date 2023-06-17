package com.example.ot.app.host.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.region.entity.City;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Host extends BaseTimeEntity {

    private String introduction;

    @OneToOne
    private Member member;

    @ManyToOne
    private City city;
}
