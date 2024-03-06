package com.project.bookstore.controller;

import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.service.role.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
