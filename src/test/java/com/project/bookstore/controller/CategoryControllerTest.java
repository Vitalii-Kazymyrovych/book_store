package com.project.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.book.BookWithoutCategoryIdsDto;
import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import jakarta.servlet.ServletException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Tests for save
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save with valid request dto")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_ValidRequestDto_ReturnsCategoryDto() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "name3",
                "description3");

        CategoryDto expected = new CategoryDto(
                3L,
                requestDto.name(),
                requestDto.description());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(), CategoryDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save with null request dto")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_NullRequestDto_ThrowsException() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(null);
        // When & Then
        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save with invalid request dto")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_InvalidRequestDto_ThrowsException() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                null,
                "description3");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()));
    }

    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Save with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void save_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "name1",
                "description1");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Tests for saveAll
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save all with valid request dtos")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveAll_ValidRequestDtos_ReturnsCategoryDtosList() throws Exception {
        // Given
        List<CreateCategoryRequestDto> requestDtos = List.of(
                new CreateCategoryRequestDto(
                        "name4",
                        "description4"),
                new CreateCategoryRequestDto(
                        "name5",
                        "description5"));
        List<CategoryDto> expected = List.of(
                new CategoryDto(
                        1L,
                        "name4",
                        "description4"),
                new CategoryDto(
                        2L,
                        "name5",
                        "description5"));
        String jsonRequest = objectMapper.writeValueAsString(requestDtos);
        // When
        MvcResult result = mockMvc.perform(post("/categories/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        // Then
        EqualsBuilder.reflectionEquals(expected.get(0), actual.get(0), "id");
        EqualsBuilder.reflectionEquals(expected.get(1), actual.get(1), "id");
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save all with null request dtos")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveAll_NullRequestDtos_ThrowsException() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(null);
        // When & Then
        mockMvc.perform(post("/categories/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save all with empty request dtos list")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveAll_EmptyRequestDtosList_ThrowsException() throws Exception {
        // Given
        List<CreateCategoryRequestDto> requestDtos = Collections.emptyList();

        String jsonRequest = objectMapper.writeValueAsString(requestDtos);
        // When & Then
        mockMvc.perform(post("/categories/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", authorities = {"user"}) // Non-admin user
    @Test
    @DisplayName("Save all with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void saveAll_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        List<CreateCategoryRequestDto> requestDtos = List.of(
                new CreateCategoryRequestDto(
                        "name4",
                        "description4"),
                new CreateCategoryRequestDto(
                        "name5",
                        "description5"));
        String jsonRequest = objectMapper.writeValueAsString(requestDtos);
        // When & Then
        mockMvc.perform(post("/categories/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Tests for findAll
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find all with valid pageable")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_ValidPageable_ReturnsCategoryDtosList() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10); // Example pageable

        List<CategoryDto> expected = List.of(
                new CategoryDto(
                        1L,
                        "name1",
                        "description1"),
                new CategoryDto(
                        2L,
                        "name2",
                        "description2")
        );
        String jsonPageable = objectMapper.writeValueAsString(pageable);
        // When
        MvcResult result = mockMvc.perform(get("/categories")
                        .content(jsonPageable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find all with null pageable")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_NullPageable_ThrowsException() throws Exception {
        //Given
        List<CategoryDto> expected = List.of(
                new CategoryDto(
                        1L,
                        "name1",
                        "description1"),
                new CategoryDto(
                        2L,
                        "name2",
                        "description2"));
        // When
        MvcResult result = mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<CategoryDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        // Then
        for (int i = 0; i < expected.size(); i++) {
            EqualsBuilder.reflectionEquals(
                    expected.get(i),
                    actual.get(i),
                    "id");
        }
    }

    @Test
    @DisplayName("Find all with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAll_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String jsonPageable = objectMapper.writeValueAsString(pageable);
        // When & Then
        mockMvc.perform(get("/categories")
                        .content(jsonPageable)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Tests for findById
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find by valid ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findById_ValidId_ReturnsCategoryDto() throws Exception {
        // Given
        Long validId = 1L;
        CategoryDto expected = new CategoryDto(
                1L,
                "name1",
                "description1");

        // When
        MvcResult result = mockMvc.perform(get("/categories/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);

        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find by invalid ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findById_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = -1L;
        // When & Then
        mockMvc.perform(get("/categories/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Find by ID with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findById_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        // When & Then
        mockMvc.perform(get("/categories/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // Tests for updateById
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by valid ID and request dto")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateById_ValidIdAndRequestDto_ReturnsCategoryDto() throws Exception {
        // Given
        Long validId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "updated name",
                "updated description");
        CategoryDto expected = new CategoryDto(
                validId,
                requestDto.name(),
                requestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(put("/categories/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        CategoryDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CategoryDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by invalid ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateById_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = 999L; // Assuming negative IDs are invalid
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "updated name",
                "updated description");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(put("/categories/" + invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by ID with null request dto")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateById_NullRequestDto_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        String jsonRequest = objectMapper.writeValueAsString(null);
        // When & Then
        mockMvc.perform(put("/categories/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "user", authorities = {"user"}) // Non-admin authority
    @Test
    @DisplayName("Update by ID with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateById_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "updated name",
                "updated description");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(put("/categories/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Tests for findAllBooksByCategoryId
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find all books by valid category ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBooksByCategoryId_ValidId_ReturnsBooksList() throws Exception {
        // Given
        Long validId = 1L;
        List<BookWithoutCategoryIdsDto> expected = List.of(
                new BookWithoutCategoryIdsDto(
                        1L,
                        "Book Title 1",
                        "Author Name 1",
                        "978-0-00-000000-1",
                        new BigDecimal("19.99"),
                        "Description for Book 1",
                        "https://coverimage1.jpg"),
                new BookWithoutCategoryIdsDto(
                        2L,
                        "Book Title 2",
                        "Author Name 2",
                        "978-0-00-000000-2",
                        new BigDecimal("29.99"),
                        "Description for Book 2",
                        "https://coverimage2.jpg")
        );
        // When
        MvcResult result = mockMvc.perform(get("/categories/" + validId + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookWithoutCategoryIdsDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                }
        );
        // Then
        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find all books by invalid category ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBooksByCategoryId_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = 999L; // Assuming negative IDs are invalid
        List<BookWithoutCategoryIdsDto> expected = List.of();
        // When
        MvcResult result = mockMvc.perform(get("/categories/" + invalidId + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookWithoutCategoryIdsDto> actual = objectMapper
                .readValue(
                        result.getResponse().getContentAsString(),
                        new TypeReference<>() {
        });
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @WithMockUser(username = "guest", authorities = {"guest"}) // Non-user authority
    @Test
    @DisplayName("Find all books by category ID with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllBooksByCategoryId_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        Pageable pageable = PageRequest.of(0, 10); // Example pageable
        // When & Then
        mockMvc.perform(get("/categories/" + validId + "/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    // Tests for deleteById
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Delete by valid ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteById_ValidId_PerformsDeletion() throws Exception {
        // Given
        Long validId = 1L;
        // When & Then
        mockMvc.perform(delete("/categories/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Delete by invalid ID")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteById_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = 999L; // Assuming negative IDs are invalid
        // When & Then
        mockMvc.perform(delete("/categories/" + invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "user", authorities = {"user"}) // Non-admin authority
    @Test
    @DisplayName("Delete by ID with unauthorized user")
    @Sql(scripts = "classpath:database/controller/category/" +
            "01-prepare-db-for-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/category/" +
            "02-clear-db-after-category-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteById_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        // When & Then
        mockMvc.perform(delete("/categories/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}