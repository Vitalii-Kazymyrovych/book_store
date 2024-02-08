package com.project.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class UrlValidator implements ConstraintValidator<Url, String> {
    private static final String URL_PATTERN = "((http|https)://)(www.)?"
            + "[a-zA-Z0-9@:%._\\+~#?&//=]"
            + "{2,256}\\.[a-z]"
            + "{2,6}\\b([-a-zA-Z0-9@:%"
            + "._\\+~#?&//=]*)";

    @Override
    public boolean isValid(String url, ConstraintValidatorContext constraintValidatorContext) {
        return url != null && Pattern.compile(URL_PATTERN).matcher(url).matches();
    }
}
