package com.example.ot.util;

import com.example.ot.app.base.dto.RsData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class Util {
    public static class spring {

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData) {
            return responseEntityOf(rsData, null);
        }

        public static <T> ResponseEntity<RsData> responseEntityOf(RsData<T> rsData, HttpHeaders headers) {
            return new ResponseEntity<>(rsData, headers, rsData.isSuccess() ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
        }
    }
}
