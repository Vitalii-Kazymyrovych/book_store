package com.project.bookstore.dto.user;

import com.project.bookstore.validation.field.match.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@FieldMatch
public record CreateUserRequestDto(
        @Email String email,
        @NotBlank @NotNull @Size(min = 8) String password,
        @NotBlank @NotNull @Size(min = 8) String repeatPassword,
        @NotBlank @NotNull String firstName,
        @NotBlank @NotNull String lastName,
        String shippingAddress) {
}
