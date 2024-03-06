package com.project.bookstore.controller;

import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import com.project.bookstore.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/update-roles")
    public UserWithRolesDto updateUserRoles(@RequestBody UpdateUserRolesRequestDto requestDto) {
        return userService.updateUserRoles(requestDto);
    }
}
