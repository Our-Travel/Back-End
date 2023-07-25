package com.example.ot.app.board.dto.request;

import com.example.ot.base.customValidator.annotation.FutureLocalDate;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CreateBoardRequest {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "여행위치를 입력해주세요.")
    private Integer regionCode;

    @NotNull(message = "여행인원을 입력해주세요.")
    @Min(value = 2, message = "여행인원은 2명 이상이어야 합니다.")
    private Integer numberOfTravelers;

    @NotNull(message = "모집기간을 입력해주세요.")
    @FutureLocalDate(message = "모집기간 시작일은 현재 날짜 이후이어야 합니다.")
    private LocalDate recruitmentPeriodStart;

    @NotNull(message = "모집기간을 입력해주세요.")
    @FutureLocalDate(message = "모집기간 종료일은 현재 날짜 이후이어야 합니다.")
    private LocalDate recruitmentPeriodEnd;

    @NotNull(message = "여행기간을 입력해주세요.")
    private LocalDate journeyPeriodStart;

    @NotNull(message = "여행기간을 입력해주세요.")
    private LocalDate journeyPeriodEnd;
}
