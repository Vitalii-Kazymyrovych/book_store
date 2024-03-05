package com.project.bookstore.dto.user;

import java.util.Set;

public record UserWithRolesDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        String shippingAddress,
        Set<Long> roleIds) {
}
