package com.project.bookstore.controller;

import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserLoginRequestDto;
import com.project.bookstore.dto.user.UserLoginResponseDto;
import com.project.bookstore.security.AuthenticationService;
import com.project.bookstore.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    public UserDto registerUser(@RequestBody @Valid CreateUserRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
