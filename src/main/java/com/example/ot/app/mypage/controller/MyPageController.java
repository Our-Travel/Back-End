package com.example.ot.app.mypage.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.app.mypage.dto.MyPageDTO;
import com.example.ot.app.mypage.service.MyPageService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Tag(name = "마이 페이지")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberService memberService;

    @Operation(summary = "마이페이지 초기화면", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<?> me(@AuthenticationPrincipal MemberContext memberContext) throws IOException {
//        RsData rsData = myPageService.showProfilePicture()
        Member member = memberService.findById(memberContext.getId()).orElse(null);
        String filePath = member.getProfileImage().getStoredFilePath();

        File file = new File(filePath);

        if (file.exists()) {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            Map<String, Object> memberContextMap = Util.json.toMap(memberContext);
            memberContextMap.put("image", base64File);

            return Util.spring.responseEntityOf(RsData.of("S-1", "마이페이지", memberContextMap));
        }
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
