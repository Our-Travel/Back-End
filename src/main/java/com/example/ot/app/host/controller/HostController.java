package com.example.ot.app.host.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.host.dto.HostInfoResponse;
import com.example.ot.app.host.dto.RegionDTO;
import com.example.ot.app.host.dto.RegisterHostDTO;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.service.HostService;
import com.example.ot.app.member.entity.Member;
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
@RequestMapping("/api/host")
public class HostController {

    private final HostService hostService;

    @Operation(summary = "호스트 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("")
    public ResponseEntity<RsData> registerHost(@Valid @RequestBody RegisterHostDTO registerHostDTO,
                                               @AuthenticationPrincipal MemberContext memberContext){
        log.info("Introduction : {} " , registerHostDTO.getIntroduction());
        log.info("HashTag : {} " , registerHostDTO.getHashTag());
        log.info("City : {} " , registerHostDTO.getCity());

        RsData<Host> host = hostService.createHost(registerHostDTO, memberContext.getId());

        if(host.isFail()){
            return Util.spring.responseEntityOf(host);
        }

        return Util.spring.responseEntityOf(host);
    }

    @Operation(summary = "호스트 등록페이지(지역정보 제공)", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> registerHost(@AuthenticationPrincipal MemberContext memberContext){
        RsData<Member> checkHostAuthority = hostService.hasHostAuthority(memberContext.getId());
        if(checkHostAuthority.isFail()){
            return Util.spring.responseEntityOf(checkHostAuthority);
        }
        RegionDTO regionDTO = hostService.getRegion();

        if(checkHostAuthority.getResultCode().equals("S-2")){
            return Util.spring.responseEntityOf(RsData.successOf(regionDTO));
        }

        HostInfoResponse hostInfoResponse = hostService.hostInfo(memberContext.getId(), regionDTO);
        return Util.spring.responseEntityOf(RsData.of("S-2", "호스트 정보 수정", hostInfoResponse));
    }

}
