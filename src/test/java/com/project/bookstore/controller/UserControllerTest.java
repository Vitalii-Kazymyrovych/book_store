package com.project.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.user.UpdateUserRolesRequestDto;
import com.project.bookstore.dto.user.UserWithRolesDto;
import jakarta.servlet.ServletException;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "classpath:database/controller/user/" +
        "01-prepare-db-for-user-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/controller/user/" +
        "02-clear-db-after-user-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Tests for updateUserRoles
    @Test
    @DisplayName("Update user roles with valid request DTO returns UserWithRolesDto")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateUserRoles_ValidRequestDto_ReturnsUserWithRolesDto() throws Exception {
        // Given
        UpdateUserRolesRequestDto requestDto = new UpdateUserRolesRequestDto(
                1L,
                List.of(1L, 2L));
        UserWithRolesDto expected = new UserWithRolesDto(
                1L,
                "finalUser@examples.org",
                "first name",
                "last name",
                "address",
                Set.of(1L, 2L));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(put("/users/update-roles")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        UserWithRolesDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        UserWithRolesDto.class);
        // Then
        assertEquals(expected, actual);
    }

    // Test for null request DTO
    @Test
    @DisplayName("Update user roles with null request DTO throws exception")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateUserRoles_NullRequestDto_ThrowsException() throws Exception {
        // Given
        UpdateUserRolesRequestDto nullRequestDto = null;
        // When & Then
        mockMvc.perform(put("/users/update-roles")
                        .content(objectMapper.writeValueAsString(nullRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Test for invalid request DTO
    @Test
    @DisplayName("Update user roles with invalid request DTO throws exception")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateUserRoles_InvalidRequestDto_ThrowsException() throws Exception {
        // Given
        UpdateUserRolesRequestDto invalidRequestDto = new UpdateUserRolesRequestDto(
                null,
                List.of(1L, 2L));
        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);
        // When & Then
        assertThrows(ServletException.class, ()
                -> mockMvc.perform(put("/users/update-roles")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn());
    }

    // Test for unauthorized user
    @Test
    @DisplayName("Update user roles with unauthorized user throws exception")
    public void updateUserRoles_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        UpdateUserRolesRequestDto requestDto = new UpdateUserRolesRequestDto(
                1L,
                List.of(1L, 2L));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(put("/update-roles")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}