package com.example.ot.app.board.dto.response;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BoardListResponse {

    private Long boardId;
    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private String recruitmentPeriodStart;
    private String recruitmentPeriodEnd;
    private String journeyPeriodStart;
    private String journeyPeriodEnd;
    private RecruitmentStatus recruitmentStatus;
    private long likeCounts;

    public static BoardListResponse fromTravelBoard(TravelBoard travelBoard){
        return BoardListResponse.builder()
                .boardId(travelBoard.getId())
                .title(travelBoard.getTitle())
                .content(travelBoard.getContent())
                .regionCode(travelBoard.getRegionCode())
                .numberOfTravelers(travelBoard.getNumberOfTravelers())
                .recruitmentPeriodStart(String.valueOf(travelBoard.getRecruitmentPeriodStart()))
                .recruitmentPeriodEnd(String.valueOf(travelBoard.getRecruitmentPeriodEnd()))
                .journeyPeriodStart(String.valueOf(travelBoard.getJourneyPeriodStart()))
                .journeyPeriodEnd(String.valueOf(travelBoard.getJourneyPeriodEnd()))
                .recruitmentStatus(travelBoard.getRecruitmentStatus())
                .build();
    }

    public void setLikeCounts(long likeCounts){
        this.likeCounts = likeCounts;
    }
}
