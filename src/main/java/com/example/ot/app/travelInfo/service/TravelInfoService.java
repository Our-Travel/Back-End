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

    public List<ShowMapDataResponse> getMapData(String contentTypeId, Long memberId) {
        List<TravelInfo> travelInfoList = travelInfoRepository.findByContentTypeId(contentTypeId);
        List<ShowMapDataResponse> showMapDataResponseList = new ArrayList<>();
        for(TravelInfo travelInfo: travelInfoList){
            ShowMapDataResponse data = ShowMapDataResponse.of(travelInfo, false);
            showMapDataResponseList.add(data);
        }
        return showMapDataResponseList;
    }

    public ShowMapDataResponse getOneMapData(Integer contentId, Long memberId) {
        TravelInfo travelInfo = travelInfoRepository.findByContentId(contentId).orElseThrow(() -> new TravelInfoException(TRAVEL_INFO_NOT_EXISTS));
        boolean likedTravelInfo = likedTravelInfoRepository.existsByMemberIdAndContentId(memberId, contentId);
        return ShowMapDataResponse.of(travelInfo, likedTravelInfo);
    }

    @Transactional
    public Code likeTravelInfo(Integer contentId, Long memberId) {
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

    public List<LikedTravelInfoResponse> getLikedTravelInfoList(Long memberId, String contentTypeId) {
        List<LikedTravelInfo> likedTravelInfoList = likedTravelInfoRepository.findByMemberIdAndContentTypeId(memberId, contentTypeId);
        List<LikedTravelInfoResponse> likedTravelInfoResponses = new ArrayList<>();
        for(LikedTravelInfo likedTravelInfo : likedTravelInfoList) {
            TravelInfo travelInfo = likedTravelInfo.getTravelInfo();
            LikedTravelInfoResponse likedTravelInfoResponse = LikedTravelInfoResponse.of(travelInfo);
            likedTravelInfoResponses.add(likedTravelInfoResponse);
        }
        return likedTravelInfoResponses;
    }
}
