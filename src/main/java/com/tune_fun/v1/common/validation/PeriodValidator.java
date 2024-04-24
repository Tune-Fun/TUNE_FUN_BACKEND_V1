package com.tune_fun.v1.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static java.lang.reflect.AccessibleObject.setAccessible;

public class PeriodValidator implements ConstraintValidator<Period, Object> {

    private String start;
    private String end;

    @Override
    public void initialize(Period constraintAnnotation) {
        start = constraintAnnotation.start();
        end = constraintAnnotation.end();
    }

    @SneakyThrows
    @Override
    public boolean isValid(Object object, ConstraintValidatorContext constraintValidatorContext) {
        Field candidate1 = object.getClass().getDeclaredField(this.start);
        Field candidate2 = object.getClass().getDeclaredField(this.end);
        setAccessible(new Field[]{candidate1, candidate2}, true);

        LocalDateTime startDateTime = (LocalDateTime) candidate1.get(object);
        LocalDateTime endDateTime = (LocalDateTime) candidate2.get(object);

        return startDateTime.isBefore(endDateTime);
    }

}
