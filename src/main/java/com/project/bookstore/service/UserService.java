package com.project.bookstore.service;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.exception.RegistrationException;

public interface UserService {
    UserDto register(CreateUserRequestDto requestDto) throws RegistrationException;
}
