package com.example.ot.app.base.rsData;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RsData<T> {
    private String resultCode;
    private String msg;
    private T data;

    public static <T> RsData<T> success(String msg, T data) {
        return new RsData<>("S", msg, data);
    }

    public static <T> RsData<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> RsData<T> fail(String msg, T data) {
        return new RsData<>("F", msg, data);
    }

    public static <T> RsData<T> fail(String msg) {
        return fail(msg, null);
    }

    @JsonIgnore
    public boolean isSuccess() {
        return resultCode.startsWith("S");
    }

    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
