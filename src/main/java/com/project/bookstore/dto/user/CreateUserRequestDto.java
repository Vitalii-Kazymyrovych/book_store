package com.project.bookstore.dto.user;

import com.project.bookstore.validation.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequestDto(
        @Email String email,
        @NotBlank @Length(min = 8) String password,
        @NotBlank @Length(min = 8) String repeatPassword,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String shippingAddress) {
}
