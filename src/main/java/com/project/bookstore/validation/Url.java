package com.project.bookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UrlValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Url {
    String message() default "Invalid cover image url. Try again."
            + " Example: https://www.example.org/";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}