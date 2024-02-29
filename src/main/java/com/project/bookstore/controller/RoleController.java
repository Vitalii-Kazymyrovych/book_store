package com.project.bookstore.controller;

import com.project.bookstore.dto.role.CreateRoleRequestDto;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public RoleDto save(@RequestBody CreateRoleRequestDto requestDto) {
        return roleService.save(requestDto);
    }
}
