package com.example.ot.app.localplaces.controller;

import com.example.ot.app.api.dto.KakaoApiResponseDTO;
import com.example.ot.app.api.service.KakaoCategorySearchService;
import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.localplaces.service.LocalPlacesService;
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

import java.io.IOException;
import java.util.List;

@Tag(name = "주변 관광지, 숙박")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/local-place")
public class LocalPlacesController {

    private final LocalPlacesService localPlacesService;
    private static final String SPOT_CATEGORY = "AT4";
    private static final String HOTEL_CATEGORY = "AD5";

    @Operation(summary = "주변 관광지", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/spot")
    public ResponseEntity<RsData> spot(@AuthenticationPrincipal MemberContext memberContext, @RequestParam double latitude, @RequestParam double longitude) {
        KakaoApiResponseDTO responseDTOList = localPlacesService.response(SPOT_CATEGORY, latitude, longitude);
        return Util.spring.responseEntityOf(RsData.successOf(responseDTOList));
    }

//    @Operation(summary = "관광지 세부정보", security = @SecurityRequirement(name = "bearerAuth"))
//    @GetMapping("/spot/{siteId}")
//    public ResponseEntity<RsData> spotId(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long siteId) {
//
//        return Util.spring.responseEntityOf(RsData.successOf(kakaoCategorySearchService.requestCategorySearch(SPOT_CATEGORY, latitude, longitude)));
//    }

}
