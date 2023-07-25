package com.example.ot.app.board.entity;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Getter
public class TravelBoard extends BaseTimeEntity {

    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private LocalDate recruitmentPeriod;
    private LocalDate journeyPeriod;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public static TravelBoard of(CreateBoardRequest createBoardRequest, Member member){
        RecruitmentStatus status = getRecruitmentStatus(createBoardRequest.getRecruitmentPeriod());
        return TravelBoard.builder()
                .title(createBoardRequest.getTitle())
                .content(createBoardRequest.getContent())
                .regionCode(createBoardRequest.getRegionCode())
                .numberOfTravelers(createBoardRequest.getNumberOfTravelers())
                .recruitmentPeriod(createBoardRequest.getRecruitmentPeriod())
                .journeyPeriod(createBoardRequest.getJourneyPeriod())
                .recruitmentStatus(status)
                .member(member)
                .build();
    }

    private static RecruitmentStatus getRecruitmentStatus(LocalDate recruitmentPeriod){
        LocalDate now = LocalDate.now();
        if(recruitmentPeriod.isAfter(now)){
            return RecruitmentStatus.UPCOMING;
        }
        return RecruitmentStatus.OPEN;
    }
}
