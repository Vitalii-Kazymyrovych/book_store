package com.project.bookstore.dto.role;

public record RoleDto(
        Long id,
        String roleName) {
    public RoleDto setRoleName(String roleName) {
        return new RoleDto(
                this.id,
                roleName);
    }
}
