package com.project.bookstore.service;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import com.project.bookstore.exception.RegistrationException;
import com.project.bookstore.mapper.UserMapper;
import com.project.bookstore.model.Role;
import com.project.bookstore.model.User;
import com.project.bookstore.repository.role.RoleRepository;
import com.project.bookstore.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final Long USER_ROLE_ID = 1L;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    @Override
    public UserDto register(CreateUserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("Such user already exists: " + requestDto);
        }
        User newUser = userMapper.toModel(requestDto);
        newUser.setRoles(Set.of(new Role(USER_ROLE_ID)));
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserWithRolesDto updateUserRoles(UpdateUserRolesRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.email()).orElseThrow();
        Set<Role> newRoles = requestDto
                .roleIds()
                .stream()
                .map(Role::new)
                .collect(Collectors.toSet());
        user.setRoles(newRoles);
        User savedUser = userRepository.save(user);
        return userMapper.toUserWithRolesDto(savedUser);
    }


}
