package com.project.bookstore.dto.user;

import java.util.List;

public record UpdateUserRolesRequestDto(
        Long id,
        List<Long> roleIds) {
}
