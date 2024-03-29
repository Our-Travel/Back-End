package com.example.ot.app.board.service;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.board.dto.request.EditBoardRequest;
import com.example.ot.app.board.dto.response.BoardListResponse;
import com.example.ot.app.board.dto.response.EditBoardResponse;
import com.example.ot.app.board.dto.response.LikedBoardResponse;
import com.example.ot.app.board.dto.response.ShowBoardResponse;
import com.example.ot.app.board.entity.LikeBoard;
import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.exception.ErrorCode;
import com.example.ot.app.board.exception.TravelBoardException;
import com.example.ot.app.board.repository.LikeBoardRepository;
import com.example.ot.app.board.repository.TravelBoardRepository;
import com.example.ot.app.chat.entity.ChatRoom;
import com.example.ot.app.chat.event.CreateChatRoomEvent;
import com.example.ot.app.chat.repository.ChatRoomAndMemberRepository;
import com.example.ot.app.chat.repository.ChatRoomRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.member.repository.ProfileImageRepository;
import com.example.ot.base.code.Code;
import com.example.ot.config.security.entity.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ot.app.board.code.TravelBoardSuccessCode.*;
import static com.example.ot.app.board.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelBoardService {

    private final MemberRepository memberRepository;
    private final TravelBoardRepository travelBoardRepository;
    private final LikeBoardRepository likeBoardRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ProfileImageRepository profileImageRepository;
    private final ChatRoomAndMemberRepository chatRoomAndMemberRepository;
    private final ApplicationEventPublisher publisher;
    private final static int pageOfSize = 10;

    @Transactional
    public void createBoard(CreateBoardRequest createBoardRequest, Long memberId) {
        verifyDate(createBoardRequest);
        Member member = memberRepository.findByMemberId(memberId);
        TravelBoard travelBoard = TravelBoard.of(createBoardRequest, member);
        travelBoardRepository.save(travelBoard);
        publisher.publishEvent(new CreateChatRoomEvent(travelBoard, member));
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

    private TravelBoard findByBoardId(Long boardId){
        return travelBoardRepository.findById(boardId)
                .orElseThrow(() -> new TravelBoardException(BOARD_NOT_EXISTS));
    }

    private LikeBoard getLikeBoardStatusByMember(Long boardId, Long memberId){
        return likeBoardRepository.findByMemberAndBoard(boardId, memberId).orElse(null);
    }

    public ShowBoardResponse getBoardInfo(Long boardId, Long memberId) {
        TravelBoard travelBoard = findByBoardId(boardId);
        boolean likeBoardStatusByMember = !ObjectUtils.isEmpty(getLikeBoardStatusByMember(boardId, memberId));
        Long roomId = chatRoomRepository.findByBoardId(boardId).orElse(null);
        return ShowBoardResponse.fromTravelBoard(travelBoard, likeBoardStatusByMember, memberId, roomId);
    }

    @Transactional
    public Code likeBoard(Long boardId, Long memberId) {
        LikeBoard verifyLikeBoard = getLikeBoardStatusByMember(boardId, memberId);
        if(ObjectUtils.isEmpty(verifyLikeBoard)){
            TravelBoard travelBoard = travelBoardRepository.findByBoardId(boardId);
            Member member = memberRepository.findByMemberId(memberId);
            LikeBoard likeBoard = LikeBoard.of(travelBoard, member);
            likeBoardRepository.save(likeBoard);
            return BOARD_LIKED;
        }
        likeBoardRepository.delete(verifyLikeBoard);
        return BOARD_LIKED_CANCELED;
    }

    public long getLikeBoardCounts(Long boardId){
        return likeBoardRepository.countByTravelBoard(boardId);
    }

    private TravelBoard getBoardWithValid(Long boardId, Long memberId){
        TravelBoard travelBoard = findByBoardId(boardId);
        Long MemberIdByBoard = travelBoard.getMemberId();
        if(!MemberIdByBoard.equals(memberId)){
            throw new TravelBoardException(BOARD_ACCESS_UNAUTHORIZED);
        }

        return travelBoard;
    }

    public EditBoardResponse getBoardInfoForEdit(Long boardId, Long memberId) {
        TravelBoard travelBoard = getBoardWithValid(boardId, memberId);
        return EditBoardResponse.fromTravelBoard(travelBoard);
    }

    @Transactional
    public void updateBoard(EditBoardRequest editBoardRequest, Long memberId, Long boardId) {
        TravelBoard travelBoard = getBoardWithValid(boardId, memberId);
        verifyDate(CreateBoardRequest.fromEditBoardRequest(editBoardRequest));
        travelBoard.updateBoard(editBoardRequest);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long memberId) {
        TravelBoard travelBoard = getBoardWithValid(boardId, memberId);
        travelBoardRepository.delete(travelBoard);
    }

    public Slice<BoardListResponse> getMyBoardList(Long lastBoardId, Long memberId) {
        Slice<TravelBoard> travelBoardList = travelBoardRepository.findMyBoardsWithKeysetPaging(lastBoardId, memberId, PageRequest.ofSize(pageOfSize));
        return getBoardListResponses(memberId, travelBoardList);
    }

    public Slice<BoardListResponse> getBoardListByRegion(Integer regionCode, Long lastBoardId, MemberContext memberContext) {
        Slice<TravelBoard> travelBoardList = travelBoardRepository.findBoardsByRegionWithKeysetPaging(regionCode, lastBoardId, PageRequest.ofSize(pageOfSize));
        Long memberId = 0L;
        if(!ObjectUtils.isEmpty(memberContext)){
            memberId = memberContext.getId();
        }
        return getBoardListResponses(memberId, travelBoardList);
    }

    private Slice<BoardListResponse> getBoardListResponses(Long memberId, Slice<TravelBoard> travelBoardList) {
        List<BoardListResponse> boardListResponses = new ArrayList<>();
        for(TravelBoard travelBoard : travelBoardList.getContent()){
            boolean likeBoardStatus = !ObjectUtils.isEmpty(getLikeBoardStatusByMember(travelBoard.getId(), memberId));
            long likeCounts = getLikeBoardCounts(travelBoard.getId());
            Long boardMemberId = travelBoard.getMemberId();
            ProfileImage profileImage = profileImageRepository.findProfileImageByMemberId(boardMemberId).orElse(null);
            Long roomId = chatRoomRepository.findByBoardId(travelBoard.getId()).orElse(null);
            Integer headCount = chatRoomAndMemberRepository.countByRoomId(roomId);
            boardListResponses.add(BoardListResponse.fromTravelBoard(travelBoard, memberId, likeBoardStatus, likeCounts, profileImage, headCount));
        }
        return new SliceImpl<>(boardListResponses, travelBoardList.getPageable(), travelBoardList.hasNext());
    }

    @Transactional
    public void closeRecruitment(Long boardId, Long memberId) {
        TravelBoard travelBoard = getBoardWithValid(boardId, memberId);
        travelBoard.updateClosingRecruitment();
    }

    public List<LikedBoardResponse> getLikedBoardList(Long memberId, Long id) {
        if(!Objects.equals(memberId, id)) {
            throw new AccessDeniedException("접근권한이 없습니다.");
        }
        List<LikeBoard> likeBoardList = likeBoardRepository.findByMemberId(memberId);
        List<LikedBoardResponse> likedBoardResponses = new ArrayList<>();
        for(LikeBoard likedBoard : likeBoardList){
            TravelBoard travelBoard = likedBoard.getTravelBoard();
            LikedBoardResponse likedBoardResponse = LikedBoardResponse.of(travelBoard);
            likedBoardResponses.add(likedBoardResponse);
        }
        return likedBoardResponses;
    }
}
