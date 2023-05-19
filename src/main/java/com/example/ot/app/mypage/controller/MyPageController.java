package com.example.ot.app.mypage.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.mypage.service.MyPageService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Tag(name = "마이 페이지")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;

    @Operation(summary = "마이페이지 초기화면", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {
        return Util.spring.responseEntityOf(RsData.successOf(memberContext));
    }

    @Operation(summary = "프로필 사진 업로드", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/upload")
    public ResponseEntity<RsData> uploadProfilePicture(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal MemberContext memberContext) {

        RsData valid = myPageService.canUploadProfilePicture(file);

        if(valid.isFail()){
            return Util.spring.responseEntityOf(valid);
        }
        RsData upload = myPageService.uploadProfilePicture(file, memberContext.getUsername());

        return Util.spring.responseEntityOf(upload);
    }
}
