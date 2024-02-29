package com.project.bookstore.service;

import com.project.bookstore.dto.role.CreateRoleRequestDto;
import com.project.bookstore.dto.role.RoleDto;

public interface RoleService {
    RoleDto save(CreateRoleRequestDto requestDto);
}
