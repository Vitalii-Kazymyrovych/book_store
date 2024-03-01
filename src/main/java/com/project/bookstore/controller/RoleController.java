package com.project.bookstore.controller;

import com.project.bookstore.dto.role.CreateRoleRequestDto;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public List<RoleDto> findAll() {
        return roleService.findAll();
    }
}
