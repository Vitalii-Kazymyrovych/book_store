package com.project.bookstore.controller;

import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.service.role.RoleService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_ROLES_DESC;
import static com.project.bookstore.config.SwaggerConstants.FIND_ALL_ROLES_SUM;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Roles management endpoints")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = FIND_ALL_ROLES_SUM, description = FIND_ALL_ROLES_DESC)
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }
}
