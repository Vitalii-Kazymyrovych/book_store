package com.project.bookstore.service;

import com.project.bookstore.dto.role.CreateRoleRequestDto;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.mapper.RoleMapper;
import com.project.bookstore.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleDto save(CreateRoleRequestDto requestDro) {
        return roleMapper.toDto(roleRepository.save(roleMapper.toModel(requestDro)));
    }
}
