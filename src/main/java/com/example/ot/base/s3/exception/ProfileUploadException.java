package com.example.ot.base.s3.exception;

import java.util.NoSuchElementException;

public class ProfileUploadException extends NoSuchElementException {

    public ProfileUploadException() {
        super("파일을 업로드해주세요.");
    }
}
