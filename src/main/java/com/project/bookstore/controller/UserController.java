package com.project.bookstore.controller;

import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.service.UserService;
import com.project.bookstore.dto.user.UserWithRolesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/update_roles")
    public UserWithRolesDto updateUserRoles(@RequestBody UpdateUserRolesRequestDto requestDto) {
        return userService.updateUserRoles(requestDto);
    }
}
