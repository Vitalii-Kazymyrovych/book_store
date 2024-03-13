package com.project.bookstore.service.user;

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
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final Long USER_ROLE_ID = 1L;
    private static final Long ADMIN_ROLE_ID = 2L;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;

    @Override
    public UserDto register(CreateUserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new RegistrationException("Such user already exists: "
                    + requestDto);
        }
        User newUser = userMapper.toModel(requestDto);
        newUser.setRoles(Set.of(new Role(USER_ROLE_ID)));
        if (userRepository.count() == 0) {
            newUser.setRoles(Set.of(
                    new Role(USER_ROLE_ID),
                    new Role(ADMIN_ROLE_ID)));
        }
        newUser.setPassword(encoder.encode(requestDto.password()));
        return userMapper.toDto(userRepository.save(newUser));
    }

    @Override
    public UserWithRolesDto updateUserRoles(UpdateUserRolesRequestDto requestDto) {
        User user = userRepository
                .findById(requestDto.id())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Can't find user by id: "
                                        + requestDto.id()));
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
