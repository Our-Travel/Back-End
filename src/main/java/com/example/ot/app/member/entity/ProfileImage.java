package com.example.ot.app.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadFileName;

    private String storedFileName;

    private String fullPath;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public ProfileImage(String uploadFileName, String storedFileName, String fullPath) {
        this.uploadFileName = uploadFileName;
        this.storedFileName = storedFileName;
        this.fullPath = fullPath;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void updateProfile(ProfileImage changeProfile) {
        this.uploadFileName = changeProfile.getUploadFileName();
        this.storedFileName = changeProfile.getStoredFileName();
        this.fullPath = changeProfile.getFullPath();
    }

}
