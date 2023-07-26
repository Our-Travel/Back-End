package com.example.ot.app.board.dto.response;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ShowBoardResponse {

    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private LocalDate recruitmentPeriodStart;
    private LocalDate recruitmentPeriodEnd;
    private LocalDate journeyPeriodStart;
    private LocalDate journeyPeriodEnd;
    private boolean boardWriter;
    private boolean likeBoardStatus;
    private RecruitmentStatus recruitmentStatus;

    public static ShowBoardResponse fromTravelBoard(TravelBoard travelBoard, boolean likeBoardStatusByMember, Long memberId) {
        boolean boardWriter = Objects.equals(travelBoard.getMember().getId(), memberId);
        return ShowBoardResponse.builder()
                .title(travelBoard.getTitle())
                .content(travelBoard.getContent())
                .regionCode(travelBoard.getRegionCode())
                .numberOfTravelers(travelBoard.getNumberOfTravelers())
                .recruitmentPeriodStart(travelBoard.getRecruitmentPeriodStart())
                .recruitmentPeriodEnd(travelBoard.getRecruitmentPeriodEnd())
                .journeyPeriodStart(travelBoard.getJourneyPeriodStart())
                .journeyPeriodEnd(travelBoard.getJourneyPeriodEnd())
                .boardWriter(boardWriter)
                .likeBoardStatus(likeBoardStatusByMember)
                .recruitmentStatus(travelBoard.getRecruitmentStatus())
                .build();
    }
}
