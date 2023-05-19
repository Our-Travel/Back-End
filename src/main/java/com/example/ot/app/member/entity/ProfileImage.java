package com.example.ot.app.member.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileImage extends BaseTimeEntity {

    @Schema(description = "프로필 사진 저장 경로")
    private String storedFilePath;
    @Schema(description = "파일 사이즈")
    private Long size;
    @Schema(description = "확장자")
    private String extension;
}
