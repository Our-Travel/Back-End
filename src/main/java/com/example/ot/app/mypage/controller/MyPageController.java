package com.example.ot.app.mypage.controller;

import com.example.ot.app.base.dto.RsData;
import com.example.ot.app.base.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "My Page", description = "마이 페이지")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    @GetMapping
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {

        return Util.spring.responseEntityOf(RsData.successOf(memberContext));
    }
}
