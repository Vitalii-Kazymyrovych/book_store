package com.project.bookstore.service.user;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserWithRolesDto;

public interface UserService {
    UserDto register(CreateUserRequestDto requestDto);

    UserWithRolesDto updateUserRoles(UpdateUserRolesRequestDto requestDto);
}
