package com.project.bookstore.service;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.project.bookstore.dto.role.RoleDto;
import com.project.bookstore.mapper.RoleMapper;
import com.project.bookstore.model.Role;
import com.project.bookstore.repository.role.RoleRepository;
import com.project.bookstore.service.role.RoleServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private RoleMapper roleMapper;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    @DisplayName("Find all with empty repository")
    public void findAll_RepositoryEmpty_ReturnsEmptyList() {
        //Given
        when(roleRepository.findAll()).thenReturn(Collections.emptyList());

        //When
        List<RoleDto> actual = roleService.findAll();

        //Then
        assertEquals(Collections.emptyList(), actual);
    }

    @Test
    @DisplayName("Find all with one role in repository")
    public void findAll_RepositoryHasOneRole_ReturnsSingleRoleDto() {
        //Given
        Role role = mock(Role.class);
        RoleDto roleDto = mock(RoleDto.class);
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto);

        //When
        List<RoleDto> actual = roleService.findAll();

        //Then
        List<RoleDto> expected = List.of(roleDto);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all with multiple roles in repository")
    public void findAll_RepositoryHasMultipleRoles_ReturnsListOfRoleDtos() {
        //Given
        Role role1 = mock(Role.class);
        RoleDto roleDto1 = mock(RoleDto.class);
        when(roleRepository.findAll()).thenReturn(List.of(role1, role1));
        when(roleMapper.toDto(any(Role.class))).thenReturn(roleDto1);
        List<RoleDto> expected = List.of(roleDto1, roleDto1);

        //When
        List<RoleDto> actual = roleService.findAll();

        //Then
        assertEquals(expected, actual);
    }
}
