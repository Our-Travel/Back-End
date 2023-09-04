package com.example.ot.app.host.controller;

import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.app.host.dto.response.EditHostResponse;
import com.example.ot.app.host.dto.response.HostCountResponse;
import com.example.ot.app.host.dto.response.HostInfoListResponse;
import com.example.ot.app.host.service.HostService;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.ot.app.host.code.HostSuccessCode.*;

@Tag(name = "호스트 설정")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/hosts")
public class HostController {

    private final HostService hostService;

    @Operation(summary = "호스트 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<RsData> registerHost(@Valid @RequestBody WriteHostInfoRequest writeHostInfoRequest,
                                               @AuthenticationPrincipal MemberContext memberContext){
        hostService.createHost(writeHostInfoRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(HOST_REGISTER));
    }

    @Operation(summary = "호스트 수정 페이지", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<RsData> editHostInfoPage(@AuthenticationPrincipal MemberContext memberContext){
        EditHostResponse hostInfo = hostService.getHostInfo(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(HOST_EDIT_PAGE, hostInfo));
    }

    @Operation(summary = "호스트 정보 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('host')")
    @PatchMapping
    public ResponseEntity<RsData> editHostInfo(@Valid @RequestBody WriteHostInfoRequest writeHostInfoRequest,
                                               @AuthenticationPrincipal MemberContext memberContext){
        hostService.updateHostInfo(writeHostInfoRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(HOST_INFO_UPDATED));
    }

    @Operation(summary = "호스트 권한 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("hasAuthority('host')")
    @DeleteMapping
    public ResponseEntity<RsData> unAuthorizeHost(@AuthenticationPrincipal MemberContext memberContext){
        hostService.removeHostAuthorize(memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(HOST_DELETED));
    }

    @Operation(summary = "각 지역의 호스트 목록", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/list")
    public ResponseEntity<RsData> showHostListByRegion(@RequestParam("regionCode") Integer regionCode,
                                                       @AuthenticationPrincipal MemberContext memberContext){
        List<HostInfoListResponse> hostInfoListResponse = hostService.getHostListByRegion(regionCode);
        return Util.spring.responseEntityOf(RsData.success(HOSTS_BY_REGION_FOUND, hostInfoListResponse));
    }

    @Operation(summary = "각 지역의 호스트 인원 수", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/map")
    public ResponseEntity<RsData> showHostCountByRegion(@AuthenticationPrincipal MemberContext memberContext){
        List<HostCountResponse> hostInfoListResponse = hostService.getHostCountByRegion();
        return Util.spring.responseEntityOf(RsData.success(HOSTS_BY_REGION_FOUND, hostInfoListResponse));
    }

}
