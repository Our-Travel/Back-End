package com.example.ot.app.travelInfo.entity;

import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class LikedTravelInfo {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne
    private TravelInfo travelInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    public LikedTravelInfo (TravelInfo travelInfo, Member member){
        this.travelInfo = travelInfo;
        this.member = member;
    }

    public static LikedTravelInfo of(TravelInfo travelInfo, Member member) {
        return new LikedTravelInfo(travelInfo, member);
    }
}
