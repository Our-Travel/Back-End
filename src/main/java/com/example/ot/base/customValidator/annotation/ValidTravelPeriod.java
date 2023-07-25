package com.example.ot.base.customValidator.annotation;

import com.example.ot.base.customValidator.ValidTravelPeriodValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = { ValidTravelPeriodValidator.class })
public @interface ValidTravelPeriod {
    String message() default "여행 기간은 모집 기간 이후이어야 합니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
