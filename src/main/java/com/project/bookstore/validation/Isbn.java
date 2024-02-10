package com.project.bookstore.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IsbnValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Isbn {
    String message() default "Invalid isbn. Try again."
            + " Example: 978-5-04-116676-2";
    Class<?>[] groups() default {};
    Class<? extends Payload> [] payload() default {};
}
