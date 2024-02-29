package com.project.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import java.util.List;

public record UpdateUserRolesRequestDto(
        @Email String email,
        List<Long> roleIds) {
}
