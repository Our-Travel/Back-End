package com.example.ot.app.travelInfo.controller;

import com.example.ot.app.travelInfo.dto.response.LikedTravelInfoResponse;
import com.example.ot.app.travelInfo.dto.response.ShowMapDataResponse;
import com.example.ot.app.travelInfo.service.TravelInfoService;
import com.example.ot.base.code.Code;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ot.app.travelInfo.code.TravelInfoSuccessCode.*;

@Tag(name = "주변 관광지, 숙박")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/local-place")
public class TravelInfoController {

    private final TravelInfoService travelInfoService;

    @Operation(summary = "지도 데이터", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> showMapData(@RequestParam(value = "contentTypeId", required = false) String contentTypeId,
                                              @AuthenticationPrincipal MemberContext memberContext){
        List<ShowMapDataResponse> showAllMapDataResponse = travelInfoService.getMapData(contentTypeId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(MAP_DATA_FOUND, showAllMapDataResponse));
    }

    @Operation(summary = "하나의 관광지 세부정보", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{contentId}")
    public ResponseEntity<RsData> travelInfoData(@PathVariable Integer contentId,
                                              @AuthenticationPrincipal MemberContext memberContext){
        ShowMapDataResponse showMapDataResponse= travelInfoService.getOneMapData(contentId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(ONE_MAP_DATA_FOUND, showMapDataResponse));
    }

    @Operation(summary = "관광지 좋아요 및 좋아요 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{contentId}")
    public ResponseEntity<RsData> likeTravelInfo(@PathVariable int contentId,
                                            @AuthenticationPrincipal MemberContext memberContext){
        Code likeTravelInfoResult = travelInfoService.likeTravelInfo(contentId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(likeTravelInfoResult));
    }

    @Operation(summary = "유저의 관광지 좋아요 리스트", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    public ResponseEntity<RsData> likedTravelInfoList(@RequestParam(value = "contentTypeId", defaultValue = "12") String contentTypeId,
                                                      @AuthenticationPrincipal MemberContext memberContext){
        List<LikedTravelInfoResponse> likedTravelInfoResult = travelInfoService.getLikedTravelInfoList(memberContext.getId(), contentTypeId);
        return Util.spring.responseEntityOf(RsData.success(TRAVEL_INFO_LIKED_LIST, likedTravelInfoResult));
    }
}
