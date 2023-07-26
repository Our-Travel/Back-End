package com.example.ot.app.board.service;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.board.dto.response.ShowBoardResponse;
import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.exception.ErrorCode;
import com.example.ot.app.board.exception.TravelBoardException;
import com.example.ot.app.board.repository.LikeBoardRepository;
import com.example.ot.app.board.repository.TravelBoardRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.example.ot.app.board.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelBoardService {

    private final MemberService memberService;
    private final TravelBoardRepository travelBoardRepository;
    private final LikeBoardRepository likeBoardRepository;

    @Transactional
    public void createBoard(CreateBoardRequest createBoardRequest, Long memberId) {
        verifyDate(createBoardRequest);
        Member member = memberService.findByMemberId(memberId);
        TravelBoard travelBoard = TravelBoard.of(createBoardRequest, member);
        travelBoardRepository.save(travelBoard);
    }

    private void verifyDate(CreateBoardRequest createBoardRequest) {
        checkDateOrder(createBoardRequest.getRecruitmentPeriodStart(), createBoardRequest.getRecruitmentPeriodEnd(), RECRUITMENT_PERIOD_INVALID);
        checkDateOrder(createBoardRequest.getRecruitmentPeriodEnd(), createBoardRequest.getJourneyPeriodStart(), TRAVEL_PERIOD_BEFORE_RECRUITMENT);
        checkDateOrder(createBoardRequest.getJourneyPeriodStart(), createBoardRequest.getJourneyPeriodEnd(), TRAVEL_PERIOD_INVALID);
    }

    private void checkDateOrder(LocalDate startDate, LocalDate endDate, ErrorCode errorCode) {
        if (endDate.isBefore(startDate)) {
            throw new TravelBoardException(errorCode);
        }
    }

    private TravelBoard findByBoardIdWithWriter(Long boardId){
        return travelBoardRepository.findByBoardIdWithWriter(boardId).orElseThrow(() -> new TravelBoardException(BOARD_NOT_EXISTS));
    }

    private boolean getLikeBoardStatusByMember(Long boardId, Long memberId){
        return likeBoardRepository.existsByMember(boardId, memberId);
    }

    public ShowBoardResponse getBoardInfo(Long boardId, Long memberId) {
        TravelBoard travelBoard = findByBoardIdWithWriter(boardId);
        boolean likeBoardStatusByMember = getLikeBoardStatusByMember(boardId, memberId);
        return ShowBoardResponse.fromTravelBoard(travelBoard, likeBoardStatusByMember, memberId);
    }
}
