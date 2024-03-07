package com.project.bookstore.validation;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class FieldMatchValidator
        implements ConstraintValidator<FieldMatch, CreateUserRequestDto> {

    @Override
    public boolean isValid(
            CreateUserRequestDto value,
            ConstraintValidatorContext constraintValidatorContext) {
        return Objects.equals(value.password(), value.repeatPassword());
    }
}
