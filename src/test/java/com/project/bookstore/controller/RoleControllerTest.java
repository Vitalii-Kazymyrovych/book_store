package com.project.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.role.RoleDto;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Tests for findAll
    @Test
    @DisplayName("Find all roles as authorized admin returns RoleDto list")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void findAll_AuthorizedAdmin_ReturnsRoleDtoList() throws Exception {
        // Given
        List<RoleDto> expectedRoles = Arrays.asList(
                new RoleDto(2L, "admin"),
                new RoleDto(1L, "user")); // Replace with the expected roles
        // When
        MvcResult result = mockMvc.perform(get("/roles") // Adjust the endpoint if necessary
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<RoleDto> actualRoles = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<RoleDto>>() {});
        // Then
        assertEquals(expectedRoles, actualRoles); // Use appropriate assertion
    }

    // Test for unauthorized user
    @Test
    @DisplayName("Find all roles as unauthorized user throws exception")
    public void findAll_UnauthorizedUser_ThrowsException() throws Exception {
        // Given & When & Then
        mockMvc.perform(get("/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}