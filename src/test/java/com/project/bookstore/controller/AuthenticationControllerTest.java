package com.project.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.user.CreateUserRequestDto;
import com.project.bookstore.dto.user.UserDto;
import com.project.bookstore.dto.user.UserLoginRequestDto;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/controller/authentication/" +
        "01-prepare-db-for-authentication-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/controller/authentication/" +
        "02-clear-db-after-authentication-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class AuthenticationControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Tests for registerUser
    @Test
    @DisplayName("Register user with valid request dto")
    public void registerUser_ValidRequestDto_ReturnsUserDto() throws Exception {
        // Given
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "user@gmail.com",
                "password",
                "password",
                "firstName",
                "lastName",
                "address");
        UserDto expected = new UserDto(
                2L,
                requestDto.email(),
                requestDto.firstName(),
                requestDto.lastName(),
                requestDto.shippingAddress());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserDto actual = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(), UserDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Register user with null request dto")
    public void registerUser_NullRequestDto_ThrowsException() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(null);
        String expected = "{\"type\":\"about:blank\",\"title\":\"Bad " +
                "Request\",\"status\":400,\"detail\":\"Failed to read " +
                "request\",\"instance\":\"/auth/registration\"}";
        // When
        MvcResult result = mockMvc.perform(post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Register user with invalid request dto")
    public void registerUser_InvalidRequestDto_ThrowsException() throws Exception {
        // Given
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                null,
                "password",
                "password",
                "firstName",
                "lastName",
                "address");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        assertThrows(ServletException.class, ()
                -> mockMvc.perform(post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn());
    }

    @Test
    @DisplayName("Register user with existing username")
    public void registerUser_ExistingUsername_ThrowsException() throws Exception {
        // Given
        CreateUserRequestDto requestDto = new CreateUserRequestDto(
                "techWizard101@innovate.co",
                "password",
                "password",
                "firstName",
                "lastName",
                "address");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        assertThrows(ServletException.class, ()
                -> mockMvc.perform(post("/auth/registration")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn());
    }

    // Tests for login
    @Test
    @DisplayName("Login with valid request dto")
    public void login_ValidRequestDto_ReturnsUserLoginResponseDto() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "user@gmail.com",
                "password");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Login with null request dto")
    public void login_NullRequestDto_ThrowsException() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(null);
        String expected = "{\"type\":\"about:blank\",\"title\":\"Bad " +
                "Request\",\"status\":400,\"detail\":\"Failed to read " +
                "request\",\"instance\":\"/auth/login\"}";
        // When
        MvcResult result = mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
        String actual = result.getResponse().getContentAsString();
        // Then - exception is expected
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Login with invalid request dto")
    public void login_InvalidRequestDto_ThrowsException() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "",
                "");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Login with non-existing user")
    public void login_NonExistingUser_ThrowsException() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "nonExistingUser@domen.com",
                "password");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login with incorrect password")
    public void login_IncorrectPassword_ThrowsException() throws Exception {
        // Given
        UserLoginRequestDto requestDto = new UserLoginRequestDto(
                "techWizard101@innovate.co",
                "incorrectPassword");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/auth/login")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}