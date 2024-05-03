package com.tune_fun.v1.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class EnumValidator implements ConstraintValidator<Enum, java.lang.Enum<?>> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(java.lang.Enum value, ConstraintValidatorContext context) {
        Object[] enumValues = this.annotation.target().getEnumConstants();
        if (enumValues != null)
            for (Object enumValue : enumValues)
                if (Objects.equals(value, enumValue)) return true;
        return false;
    }
}
