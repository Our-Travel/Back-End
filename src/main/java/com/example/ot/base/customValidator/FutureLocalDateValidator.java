package com.example.ot.base.customValidator;

import com.example.ot.base.customValidator.annotation.FutureLocalDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FutureLocalDateValidator implements ConstraintValidator<FutureLocalDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        return value != null && !value.isBefore(LocalDate.now());
    }
}