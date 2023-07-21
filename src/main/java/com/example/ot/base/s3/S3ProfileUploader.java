package com.example.ot.base.s3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.ot.app.member.entity.ProfileImage;
import com.example.ot.base.s3.exception.ProfileUploadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3ProfileUploader {
    private final AmazonS3Client amazonS3Client;
    @Value("${custom.naver-cloud.s3.bucket}")
    private String bucket;

    @Value("${custom.naver-cloud.s3.profileDir}")
    private String dir;

    public ProfileImage uploadFile(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile).orElseThrow(ProfileUploadException::new);  // 파일 변환할 수 없으면 에러

        return upload(uploadFile, dir);
    }

    /* 업로드할때 파일이 로컬에 없으면
     Unable to calculate MD5 hash: [파일명] (No such file or directory) 에러가 발생하기 때문에
     로컬에 파일 업로드를 한다. s3에 업로드하고 removeNewFile() 로 지워준다.
     */
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }

    private ProfileImage upload(File uploadFile, String folderName) {
        String uploadFileName = uploadFile.getName();
        String[] split = uploadFileName.split("\\."); //확장자 추출하기 위해서 split
        String storedFileName = UUID.randomUUID().toString() + "." + split[1];
        String fileName = folderName + "/" + storedFileName;   // S3에 저장된 파일 이름

        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile); //로컬에 저장한 파일 삭제
        return new ProfileImage(uploadFileName, storedFileName, uploadImageUrl);
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    private void deleteS3(String deleteFileName){
        deleteFileName = dir + "/" + deleteFileName;
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, deleteFileName));
    }

    public ProfileImage updateFile(String deletedFileName, MultipartFile updateFile) throws IOException {
        //기존에 올렸던 거 삭제
        deleteS3(deletedFileName);
        return uploadFile(updateFile);
    }
}

