package com.example.ot.app.travelInfo.controller;

import com.example.ot.app.chat.dto.response.ShowChatRoomResponse;
import com.example.ot.app.travelInfo.dto.response.ShowMapDataResponse;
import com.example.ot.app.travelInfo.service.TravelInfoService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.ot.app.chat.code.ChatSuccessCode.CHATROOM_ENTERED;

@Tag(name = "주변 관광지, 숙박")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/local-place")
public class TravelInfoController {

    private final TravelInfoService travelInfoService;

    @Operation(summary = "지도 데이터", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> showMapData(@AuthenticationPrincipal MemberContext memberContext){
        List<ShowMapDataResponse> showChatRoomResponse= travelInfoService.getMapData();
        return Util.spring.responseEntityOf(RsData.success(CHATROOM_ENTERED, showChatRoomResponse));
    }


//
//    private final LocalPlacesService localPlacesService;
//    private static final String SPOT_CATEGORY = "AT4";
//    private static final String HOTEL_CATEGORY = "AD5";
//
//    @Operation(summary = "주변 관광지 제공", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/spot")
//    public ResponseEntity<RsData> spot(@AuthenticationPrincipal MemberContext memberContext, @RequestParam double latitude, @RequestParam double longitude) {
//        if(localPlacesService.canResponseSpot(latitude, longitude)){
//            return Util.spring.responseEntityOf(RsData.of("F-1", "위도 또는 경도값이 없습니다."));
//        }
//        KakaoApiResponseDTO responseDTOList = localPlacesService.response(SPOT_CATEGORY, latitude, longitude);
//        return Util.spring.responseEntityOf(RsData.successOf(responseDTOList));
//    }
//
//    @Operation(summary = "하나의 관광지 세부정보", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/spot/{siteId}")
//    public ResponseEntity<RsData> spotId(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long siteId) {
//        Spot spot = localPlacesService.findSpotById(siteId);
//        return Util.spring.responseEntityOf(RsData.successOf(spot));
//    }
//
//    @Operation(summary = "주변 숙박 제공", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/hotel")
//    public ResponseEntity<RsData> hotel(@AuthenticationPrincipal MemberContext memberContext, @RequestParam double latitude, @RequestParam double longitude) {
//        if(localPlacesService.canResponseSpot(latitude, longitude)){
//            return Util.spring.responseEntityOf(RsData.of("F-1", "위도 또는 경도값이 없습니다."));
//        }
//        KakaoApiResponseDTO responseDTOList = localPlacesService.response(HOTEL_CATEGORY, latitude, longitude);
//        return Util.spring.responseEntityOf(RsData.successOf(responseDTOList));
//    }
//
//    @Operation(summary = "하나의 숙박 세부정보", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/hotel/{hotelId}")
//    public ResponseEntity<RsData> hotelId(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long hotelId) {
//        Hotel hotel = localPlacesService.findHotelById(hotelId);
//        return Util.spring.responseEntityOf(RsData.successOf(hotel));
//    }
//
}
