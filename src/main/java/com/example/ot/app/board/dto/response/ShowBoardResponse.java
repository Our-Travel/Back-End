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

    private Long boardId;
    private String title;
    private String content;
    private Integer regionCode;
    private Integer numberOfTravelers;
    private String recruitmentPeriodStart;
    private String recruitmentPeriodEnd;
    private String journeyPeriodStart;
    private String journeyPeriodEnd;
    private boolean boardWriter;
    private boolean likeBoardStatus;
    private RecruitmentStatus recruitmentStatus;

    public static ShowBoardResponse fromTravelBoard(TravelBoard travelBoard, boolean likeBoardStatusByMember, Long memberId) {
        boolean boardWriter = Objects.equals(travelBoard.getMember().getId(), memberId);
        return ShowBoardResponse.builder()
                .boardId(travelBoard.getId())
                .title(travelBoard.getTitle())
                .content(travelBoard.getContent())
                .regionCode(travelBoard.getRegionCode())
                .numberOfTravelers(travelBoard.getNumberOfTravelers())
                .recruitmentPeriodStart(String.valueOf(travelBoard.getRecruitmentPeriodStart()))
                .recruitmentPeriodEnd(String.valueOf(travelBoard.getRecruitmentPeriodEnd()))
                .journeyPeriodStart(String.valueOf(travelBoard.getJourneyPeriodStart()))
                .journeyPeriodEnd(String.valueOf(travelBoard.getJourneyPeriodEnd()))
                .boardWriter(boardWriter)
                .likeBoardStatus(likeBoardStatusByMember)
                .recruitmentStatus(travelBoard.getRecruitmentStatus())
                .build();
    }
}
