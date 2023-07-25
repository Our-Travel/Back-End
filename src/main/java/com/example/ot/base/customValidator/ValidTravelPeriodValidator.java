package com.example.ot.base.customValidator;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.base.customValidator.annotation.ValidTravelPeriod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidTravelPeriodValidator implements ConstraintValidator<ValidTravelPeriod, CreateBoardRequest> {

    @Override
    public void initialize(ValidTravelPeriod constraintAnnotation) {
    }

    @Override
    public boolean isValid(CreateBoardRequest request, ConstraintValidatorContext context) {
        if (request.getRecruitmentPeriod() == null || request.getJourneyPeriod() == null) {
            return true;
        }

        return !request.getJourneyPeriod().isBefore(request.getRecruitmentPeriod());
    }
}
