package com.example.ot.app.mypage.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.mypage.entity.ProfileImage;
import com.example.ot.app.mypage.repository.ProfileImageRepository;
import com.example.ot.config.AppConfig;
import com.example.ot.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MyPageService {

    private final ProfileImageRepository profileImageRepository;
    private final MemberRepository memberRepository;
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png");

    // 프로필 사진 업로드 유효성 검사
    public RsData canUploadProfilePicture(MultipartFile file) {
        if (file.isEmpty()) {
            return RsData.of("F-1", "파일을 선택해주세요.");
        }
        if (file.getSize() > AppConfig.getMAX_FILE_SIZE()) {
            return RsData.of("F-1", "파일 크기는 최대 " + AppConfig.getMAX_FILE_SIZE() + "바이트여야 합니다.");
        }
        // 업로드 된 파일의 원본 파일 이름을 가져옴.
        String originalFileName = file.getOriginalFilename();

        String fileExtension = getFileExtension(originalFileName);
        if (!isAllowedExtension(fileExtension)) {
            return RsData.of("F-1", "지원되지 않는 파일 형식입니다. 허용된 형식은 " + ALLOWED_EXTENSIONS + "입니다.");
        }
        return RsData.of("S-1", "파일 업로드 가능합니다.");
    }

    // 파일의 확장자를 가져옴.
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    // 올바른 확장자인지 체크
    private boolean isAllowedExtension(String extension) {
        return ALLOWED_EXTENSIONS.contains(extension);
    }

    // 프로필 사진 업로드
    @Transactional
    public RsData uploadProfilePicture(MultipartFile file, String username) {

        int dotIndex = file.getOriginalFilename().lastIndexOf(".");
        String extension = file.getOriginalFilename().substring(dotIndex); // 파일 확장자 (.png 또는 .jpeg)
        String newFileName = username.split("@")[0] + extension;

        try {
            // 파일을 저장할 경로를 지정
            String uploadPath = AppConfig.getUploadPath() + "profileImage/" + newFileName;
            File fileDir = new File(uploadPath);
            // 폴더가 없을 경우 폴더 생성
            if(!fileDir.exists()){
                System.out.println("파일 생성");
                fileDir.mkdirs();
            }
            // 파일을 지정된 경로에 저장
            file.transferTo(fileDir);
            // 프로필 이미지 저장
            ProfileImage profileImage = ProfileImage.builder()
                    .size(file.getSize())
                    .storedFilePath(uploadPath)
                    .extension(extension)
                    .build();
            profileImageRepository.save(profileImage);
            // 프로필 이미지와 member 연관관계
            Member member = memberRepository.findByUsername(username).orElse(null);
            member.setProfileImage(profileImage);
            memberRepository.save(member);
            return RsData.of("S-1", "파일업로드 완료");
        } catch (IOException e) {
            e.printStackTrace();
            return RsData.of("F-1", "파일업로드 오류");
        }
    }
}
