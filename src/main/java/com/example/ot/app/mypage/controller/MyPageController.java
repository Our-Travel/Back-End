package com.example.ot.app.mypage.controller;

import com.example.ot.app.base.rsData.RsData;
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

@Tag(name = "마이 페이지")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage")
public class MyPageController {

    @Operation(summary = "마이페이지 초기화면", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext) {
        return Util.spring.responseEntityOf(RsData.successOf(memberContext));
    }

    @PostMapping("/upload")
    public ResponseEntity<RsData> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        // 파일 업로드 처리 로직을 작성합니다.
        // 이 예제에서는 업로드 된 파일을 특정 디렉토리에 저장하는 것으로 가정합니다.
        // 실제로는 파일의 유효성 검사, 파일 저장 경로 등 추가적인 로직이 필요할 수 있습니다.

        try {
            // 업로드 된 파일의 원본 파일 이름을 가져옵니다.
            String originalFileName = file.getOriginalFilename();

            // 파일을 저장할 경로를 지정합니다.
            String uploadPath = "c:/Temp/ot/" + originalFileName;

            // 파일을 지정된 경로에 저장합니다.
            file.transferTo(new File(uploadPath));

            return Util.spring.responseEntityOf(                RsData.of(
                    "S-1",
                    "파일업로드 완료"
            ));
        } catch (IOException e) {
            e.printStackTrace();
            return Util.spring.responseEntityOf(RsData.of("F-1", "파일업로드 오류"));
        }
    }
}
