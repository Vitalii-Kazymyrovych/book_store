package com.project.bookstore.service;

import com.project.bookstore.dto.user.UserLoginRequestDto;
import com.project.bookstore.dto.user.UserLoginResponseDto;
import com.project.bookstore.security.AuthenticationService;
import com.project.bookstore.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    @DisplayName("Authenticate with valid credentials")
    public void authenticate_ValidCredentials_UserLoginResponseDtoReturned() {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto("user@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(authentication);
        when(authentication.getName()).thenReturn("user@example.com");
        when(jwtUtil.generateToken("user@example.com")).thenReturn("token");
        UserLoginResponseDto expected = new UserLoginResponseDto("token");

        // When
        UserLoginResponseDto actual = authenticationService.authenticate(requestDto);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Authenticate with invalid credentials")
    public void authenticate_InvalidCredentials_BadCredentialsExceptionThrown() {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "user@example.com",
                "wrongpassword");
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));
        String expected = "Bad credentials";

        // When
        Exception actual = assertThrows(BadCredentialsException.class,
                () -> authenticationService.authenticate(requestDto));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Authenticate with null credentials")
    public void authenticate_NullCredentials_AuthenticationExceptionThrown() {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(null, null);
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthenticationCredentialsNotFoundException("Credentials not found"));
        String expected = "Credentials not found";

        // When
        Exception actual = assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> authenticationService.authenticate(requestDto)
        );

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Authenticate with empty credentials")
    public void authenticate_EmptyCredentials_AuthenticationExceptionThrown() {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto("", "");
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthenticationCredentialsNotFoundException("Empty credentials"));
        String expected = "Empty credentials";

        // When
        Exception actual = assertThrows(
                AuthenticationCredentialsNotFoundException.class,
                () -> authenticationService.authenticate(requestDto)
        );

        // Then
        assertEquals(expected, actual.getMessage());
    }
}