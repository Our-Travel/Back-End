package com.example.ot.app.host.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.host.dto.request.RegisterHostRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.service.HostService;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "호스트 설정")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hosts")
public class HostController {

    private final HostService hostService;

    @Operation(summary = "호스트 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("")
    public ResponseEntity<RsData> registerHost(@Valid @RequestBody RegisterHostRequest registerHostRequest,
                                               @AuthenticationPrincipal MemberContext memberContext){
        hostService.createHost(registerHostRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success("Host 등록이 완료되었습니다."));
    }

    @Operation(summary = "호스트 수정 페이지", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> editHostInfo(@AuthenticationPrincipal MemberContext memberContext){
        EditHostResponse memberHostInfo = hostService.getMemberHostInfo(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success("Host 수정 페이지로 이동합니다.", memberHostInfo));
    }



}
