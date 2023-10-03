package com.example.ot.app.board.dto.response;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.Objects;

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
    private String writer;
    private boolean validWriter;
    private boolean likeBoardStatus;
    private long likeCounts;
    private String profileImageFullPath;
    private Integer headCount;

    public static BoardListResponse fromTravelBoard(TravelBoard travelBoard, Long memberId, boolean likeBoardStatus, long likeCounts, ProfileImage profileImage, Integer headCount){
        Member writer = travelBoard.getMember();
        boolean validWriter = Objects.equals(writer.getId(), memberId);
        String profileImageFullPath = null;
        if(!ObjectUtils.isEmpty(profileImage)){
            profileImageFullPath = profileImage.getFullPath();
        }
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
                .writer(writer.getNickName())
                .validWriter(validWriter)
                .likeBoardStatus(likeBoardStatus)
                .likeCounts(likeCounts)
                .profileImageFullPath(profileImageFullPath)
                .headCount(headCount)
                .build();
    }
}
