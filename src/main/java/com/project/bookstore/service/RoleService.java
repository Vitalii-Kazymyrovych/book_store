package com.project.bookstore.service;

import com.project.bookstore.dto.role.RoleDto;
import java.util.List;

public interface RoleService {
    List<RoleDto> findAll();

    void fillRolesTable();
}
