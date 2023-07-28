package com.example.ot.app.board.dto.response;

import com.example.ot.app.board.entity.TravelBoard;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EditBoardResponse {

    private Long boardId;
    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private String recruitmentPeriodStart;
    private String recruitmentPeriodEnd;
    private String journeyPeriodStart;
    private String journeyPeriodEnd;

    public static EditBoardResponse fromTravelBoard(TravelBoard travelBoard) {
        return EditBoardResponse.builder()
                .boardId(travelBoard.getId())
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
