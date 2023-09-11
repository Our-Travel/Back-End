package com.example.ot.app.travelInfo.service;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.travelInfo.dto.response.LikedTravelInfoResponse;
import com.example.ot.app.travelInfo.dto.response.ShowMapDataResponse;
import com.example.ot.app.travelInfo.entity.LikedTravelInfo;
import com.example.ot.app.travelInfo.entity.TravelInfo;
import com.example.ot.app.travelInfo.exception.TravelInfoException;
import com.example.ot.app.travelInfo.repository.LikedTravelInfoRepository;
import com.example.ot.app.travelInfo.repository.TravelInfoRepository;
import com.example.ot.base.code.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.ot.app.travelInfo.code.TravelInfoSuccessCode.*;
import static com.example.ot.app.travelInfo.exception.ErrorCode.*;
import static com.example.ot.base.code.BasicErrorCode.UNAUTHORIZED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TravelInfoService {

    private final TravelInfoRepository travelInfoRepository;
    private final MemberRepository memberRepository;
    private final LikedTravelInfoRepository likedTravelInfoRepository;

    public List<ShowMapDataResponse> getMapData() {
        List<TravelInfo> travelInfoList = travelInfoRepository.findAll();
        List<ShowMapDataResponse> showMapDataResponseList = new ArrayList<>();
        for(TravelInfo travelInfo: travelInfoList){
            ShowMapDataResponse data = ShowMapDataResponse.of(travelInfo);
            showMapDataResponseList.add(data);
        }
        return showMapDataResponseList;
    }

    public ShowMapDataResponse getOneMapData(int contentId) {
        TravelInfo travelInfo = travelInfoRepository.findByContentId(contentId).orElseThrow(() -> new TravelInfoException(TRAVEL_INFO_NOT_EXISTS));
        return ShowMapDataResponse.of(travelInfo);
    }

    @Transactional
    public Code likeTravelInfo(int contentId, Long memberId) {
        LikedTravelInfo verifyLikedTravelInfo = likedTravelInfoRepository.findByContentIdAndMember(contentId, memberId).orElse(null);
        if(ObjectUtils.isEmpty(verifyLikedTravelInfo)) {
            TravelInfo travelInfo = travelInfoRepository.findByContentId(contentId).orElseThrow(() -> new TravelInfoException(TRAVEL_INFO_NOT_EXISTS));
            Member member = memberRepository.findByMemberId(memberId);
            LikedTravelInfo likedTravelInfo = LikedTravelInfo.of(travelInfo, member);
            likedTravelInfoRepository.save(likedTravelInfo);
            return TRAVEL_INFO_LIKED;
        }
        likedTravelInfoRepository.delete(verifyLikedTravelInfo);
        return TRAVEL_INFO_LIKED_CANCELED;
    }

    public List<LikedTravelInfoResponse> getLikedTravelInfoList(Long memberId, Long id) {
        if(!Objects.equals(memberId, id)) {
            throw new AccessDeniedException("접근권한이 없습니다.");
        }
        List<LikedTravelInfo> likedTravelInfoList = likedTravelInfoRepository.findByMemberId(memberId);
        List<LikedTravelInfoResponse> likedTravelInfoResponses = new ArrayList<>();
        for(LikedTravelInfo likedTravelInfo : likedTravelInfoList) {
            TravelInfo travelInfo = likedTravelInfo.getTravelInfo();
            LikedTravelInfoResponse likedTravelInfoResponse = LikedTravelInfoResponse.of(travelInfo);
            likedTravelInfoResponses.add(likedTravelInfoResponse);
        }
        return likedTravelInfoResponses;
    }
}
