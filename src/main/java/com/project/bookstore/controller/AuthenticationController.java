package com.project.bookstore.controller;

import com.project.bookstore.config.SwaggerConstants;
import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserLoginRequestDto;
import com.project.bookstore.dto.user.UserLoginResponseDto;
import com.project.bookstore.security.AuthenticationService;
import com.project.bookstore.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication management endpoints")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = SwaggerConstants.REGISTER_USER_SUM,
            description = SwaggerConstants.REGISTER_USER_DESC)
    public UserDto registerUser(@RequestBody @Valid CreateUserRequestDto requestDto) {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(summary = SwaggerConstants.LOGIN_SUM,
            description = SwaggerConstants.LOGIN_DESC)
    public UserLoginResponseDto login(@RequestBody @Valid UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
