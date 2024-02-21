package com.project.bookstore.service;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;

public interface UserService {
    UserDto register(CreateUserRequestDto requestDto);
}
