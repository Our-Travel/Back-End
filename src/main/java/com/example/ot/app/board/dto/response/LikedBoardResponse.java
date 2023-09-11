package com.example.ot.app.board.dto.response;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class LikedBoardResponse {

    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private String recruitmentPeriodStart;
    private String recruitmentPeriodEnd;
    private String journeyPeriodStart;
    private String journeyPeriodEnd;

    @Enumerated(EnumType.STRING)
    private RecruitmentStatus recruitmentStatus;


    public static LikedBoardResponse of(TravelBoard travelBoard) {
        return LikedBoardResponse.builder()
                .title(travelBoard.getTitle())
                .content(travelBoard.getContent())
                .regionCode(travelBoard.getRegionCode())
                .numberOfTravelers(travelBoard.getNumberOfTravelers())
                .recruitmentPeriodStart(String.valueOf(travelBoard.getRecruitmentPeriodStart()))
                .recruitmentPeriodEnd(String.valueOf(travelBoard.getRecruitmentPeriodEnd()))
                .journeyPeriodStart(String.valueOf(travelBoard.getJourneyPeriodStart()))
                .journeyPeriodEnd(String.valueOf(travelBoard.getJourneyPeriodEnd()))
                .build();
    }
}
