package com.project.bookstore.service;

import com.project.bookstore.dto.role.CreateRoleRequestDto;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.mapper.RoleMapper;
import com.project.bookstore.model.Role;
import com.project.bookstore.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<RoleDto> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toDto)
                .toList();
    }

    @Override
    public void fillRolesTable() {
        long rolesQuantity = roleRepository.count();
        if (rolesQuantity == 0 || rolesQuantity < Role.RoleName.values().length) {
            Arrays.stream(Role.RoleName.values())
                    .map(Role.RoleName::toString)
                    .map(CreateRoleRequestDto::new)
                    .map(roleMapper::toModel)
                    .toList()
                    .forEach(roleRepository::save);
        }
    }
}
