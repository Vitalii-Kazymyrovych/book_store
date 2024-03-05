package com.project.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @Email String email,
        @Length(min = 8, max = 20) String password) {
}
