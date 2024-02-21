package com.project.bookstore.dto.user;

import com.project.bookstore.validation.FieldMatch;
import jakarta.validation.constraints.*;

@FieldMatch
public record CreateUserRequestDto(
        @Email String email,
        @NotBlank @NotNull @Size(min = 8) String password,
        @NotBlank @NotNull @Size(min = 8) String repeatPassword,
        @NotBlank @NotNull String firstName,
        @NotBlank @NotNull String lastName,
        String shippingAddress) {
}
