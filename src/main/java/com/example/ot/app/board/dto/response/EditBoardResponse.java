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

    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private LocalDate recruitmentPeriodStart;
    private LocalDate recruitmentPeriodEnd;
    private LocalDate journeyPeriodStart;
    private LocalDate journeyPeriodEnd;

    public static EditBoardResponse fromTravelBoard(TravelBoard travelBoard) {
        return EditBoardResponse.builder()
                .title(travelBoard.getTitle())
                .content(travelBoard.getContent())
                .regionCode(travelBoard.getRegionCode())
                .numberOfTravelers(travelBoard.getNumberOfTravelers())
                .recruitmentPeriodStart(travelBoard.getRecruitmentPeriodStart())
                .recruitmentPeriodEnd(travelBoard.getRecruitmentPeriodEnd())
                .journeyPeriodStart(travelBoard.getJourneyPeriodStart())
                .journeyPeriodEnd(travelBoard.getJourneyPeriodEnd())
                .build();
    }
}
