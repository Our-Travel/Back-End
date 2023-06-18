package com.example.ot.app.host.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.host.dto.HostDTO;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.service.HostService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "호스트 설정")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/host")
public class HostController {

    private final HostService hostService;

    @Operation(summary = "호스트 등록", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("")
    public ResponseEntity<RsData> registerHost(@Valid @RequestBody HostDTO.registerHostDTO registerHostDTO,
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

}
