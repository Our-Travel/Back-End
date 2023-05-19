package com.example.ot.app.mypage.controller;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.service.MemberService;
import com.example.ot.app.mypage.service.MyPageService;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Arrays;
import java.util.List;

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

        String newFileName = memberContext.getUsername().split("@")[0];

        File file = new File(filePath);

        if(file.exists()){
            Resource resource = new FileSystemResource(file);
            MediaType mediaType = determineMediaType(newFileName);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
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

    private MediaType determineMediaType(String fileName) {
        String fileExtension = getFileExtension(fileName);
        if (fileExtension.equalsIgnoreCase("jpg") || fileExtension.equalsIgnoreCase("jpeg")) {
            return MediaType.IMAGE_JPEG;
        } else if (fileExtension.equalsIgnoreCase("png")) {
            return MediaType.IMAGE_PNG;
        }
        // 기본값으로 IMAGE_JPEG 반환
        return MediaType.IMAGE_JPEG;
    }
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}
