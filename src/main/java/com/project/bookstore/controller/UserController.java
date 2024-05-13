package com.project.bookstore.controller;

import com.project.bookstore.config.SwaggerConstants;
import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import com.project.bookstore.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.project.bookstore.config.SwaggerConstants.UPDATE_USER_ROLES_DESC;
import static com.project.bookstore.config.SwaggerConstants.UPDATE_USER_ROLES_SUM;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users management endpoints")
public class UserController {
    private final UserService userService;

    @PutMapping("/update-roles")
    @PreAuthorize("hasAuthority('admin')")
    @Operation(summary = UPDATE_USER_ROLES_SUM, description = UPDATE_USER_ROLES_DESC)
    public UserWithRolesDto updateUserRoles(@RequestBody UpdateUserRolesRequestDto requestDto) {
        return userService.updateUserRoles(requestDto);
    }
}
