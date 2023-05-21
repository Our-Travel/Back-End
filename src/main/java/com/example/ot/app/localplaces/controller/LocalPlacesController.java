package com.example.ot.app.localplaces.controller;

import com.example.ot.app.base.rsData.RsData;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Tag(name = "주변 관광지, 숙박")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/local-place")
public class LocalPlacesController {

    @Operation(summary = "주변 관광지", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/site")
    public ResponseEntity<RsData> spot(@AuthenticationPrincipal MemberContext memberContext, @RequestParam double latitude, @RequestParam double longitude) {

        return Util.spring.responseEntityOf(RsData.successOf(memberContext));
    }

}
