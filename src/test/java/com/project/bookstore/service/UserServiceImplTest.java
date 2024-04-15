package com.project.bookstore.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.exception.RegistrationException;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.model.Role;
import com.project.bookstore.model.User;
import com.project.bookstore.repository.user.UserRepository;
import com.project.bookstore.service.user.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private UserServiceImpl userService;

    // Register
    @Test
    @DisplayName("Register with non-existing user")
    public void register_NewUser_UserDtoReturned() {
        // Given
        CreateUserRequestDto requestDto = mock(CreateUserRequestDto.class);
        User newUser = mock(User.class);
        UserDto expected = mock(UserDto.class);
        when(requestDto.email()).thenReturn("newUser@example.com");
        when(requestDto.password()).thenReturn("password");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.count()).thenReturn(1L);
        when(userMapper.toModel(requestDto)).thenReturn(newUser);
        when(encoder.encode(requestDto.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expected);

        // When
        UserDto actual = userService.register(requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Register with existing user")
    public void register_UserAlreadyExists_RegistrationExceptionThrown() {
        // Given
        CreateUserRequestDto requestDto = mock(CreateUserRequestDto.class);
        when(requestDto.email()).thenReturn("existingUser@example.com");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mock(User.class)));
        String expected = "Such user already exists: " + requestDto;

        // When
        Exception actual = assertThrows(RegistrationException.class,
                () -> userService.register(requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Register with empty users table")
    public void register_FirstUser_UserDtoWithAdminRoleReturned() {
        // Given
        CreateUserRequestDto requestDto = mock(CreateUserRequestDto.class);
        User firstUser = mock(User.class);
        UserDto expectedDto = mock(UserDto.class);
        when(requestDto.email()).thenReturn("admin@example.com");
        when(requestDto.password()).thenReturn("adminPassword");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.count()).thenReturn(0L);
        when(userMapper.toModel(requestDto)).thenReturn(firstUser);
        when(encoder.encode(requestDto.password())).thenReturn("encodedAdminPassword");
        when(userRepository.save(any(User.class))).thenReturn(firstUser);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedDto);

        // When
        UserDto actualDto = userService.register(requestDto);

        // Then
        assertEquals(expectedDto, actualDto);
    }

    // Update user roles
    @Test
    @DisplayName("Update user roles with existing user and valid roles")
    public void updateUserRoles_UserExistsAndValidRoles_UserWithRolesDtoReturned() {
        // Given
        Long userId = 1L;
        UpdateUserRolesRequestDto requestDto = mock(UpdateUserRolesRequestDto.class);
        User user = mock(User.class);
        UserWithRolesDto expected = mock(UserWithRolesDto.class);
        List<Long> roleIds = List.of(1L);
        Set<Role> newRoles = roleIds.stream()
                .map(Role::new)
                .collect(Collectors.toSet());
        when(requestDto.id()).thenReturn(userId);
        when(requestDto.roleIds()).thenReturn(roleIds);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toUserWithRolesDto(user)).thenReturn(expected);

        // When
        UserWithRolesDto actual = userService.updateUserRoles(requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update user roles with non-existing user and valid roles")
    public void updateUserRoles_UserDoesNotExist_EntityNotFoundExceptionThrown() {
        // Given
        Long userId = 2L;
        UpdateUserRolesRequestDto requestDto = mock(UpdateUserRolesRequestDto.class);
        when(requestDto.id()).thenReturn(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        String expected = "Can't find user by id: " + userId;

        // When
        EntityNotFoundException actual = assertThrows(EntityNotFoundException.class,
                () -> userService.updateUserRoles(requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }
}