package com.project.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.book.BookDto;
import com.project.bookstore.dto.book.CreateBookRequestDto;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Find all
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find all with existing pageable")
    public void findAll_PageableIsPresent_ReturnsBookList() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDto> expected = List.of(mock(BookDto.class), mock(BookDto.class));

        MvcResult result = mockMvc.perform(get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", String.valueOf(pageable.getPageNumber()))
                        .param("size", String.valueOf(pageable.getPageSize())))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), List.class);

        assertEquals(expected.size(), actual.size());
    }

    // Search
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Search with valid search parameters")
    public void search_SearchParametersAreValid_ReturnsBookList() throws Exception {
        List<BookDto> expected = List.of(mock(BookDto.class));

        MvcResult result = mockMvc.perform(get("/books/search?titles=" +
                        "Book title 1&authors&prices=&isbns=")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertEquals(expected.size(), actual.size());
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Search with invalid search parameters")
    public void search_SearchParametersAreInvalid_ThrowsBadRequestException() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/search?titles=" +
                        "Book title 10&authors&prices=&isbns=")
                        .content("")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {});

        assertTrue(actual.isEmpty());
    }

    // Find by id
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find by ID when ID exists")
    public void findById_IdExists_ReturnsBookDto() throws Exception {
        Long existingId = 1L;
        BookDto expected = new BookDto(
                existingId,
                "Book Title 1",
                "Author Name 1",
                "978-0-00-000000-1",
                new BigDecimal("19.99"),
                "Description for Book 1",
                "https://coverimage1.jpg",
                new HashSet<>(List.of(1L))
        );

        MvcResult result = mockMvc.perform(get("/books/" + existingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "user", authorities = {"user"})
    @Test
    @DisplayName("Find by ID when ID does not exist")
    public void findById_IdDoesNotExist_ThrowsNotFoundException() throws Exception {
        Long nonExistingId = 999L;
        String expected = "Cant find book by id: " + nonExistingId;

        MvcResult result = mockMvc.perform(get("/books/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String actual = result.getResponse().getContentAsString();
        assertEquals(expected, actual);
    }

    // Test methods for save
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save with valid request dto")
    public void save_ValidRequestDto_ReturnsBookDto() throws Exception {
        // Given
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Book Title 3",
                "Author Name 3",
                "978-0-00-000000-3",
                new BigDecimal("39.99"),
                "Description for Book 3",
                "https://coverimage3.jpg",
                List.of(1L));

        BookDto expected = new BookDto(
                3L,
                requestDto.title(),
                requestDto.author(),
                requestDto.isbn(),
                requestDto.price(),
                requestDto.description(),
                requestDto.coverImage(),
                new HashSet<>(requestDto.categoryIds()));
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        BookDto actual = objectMapper
                .readValue(result.getResponse()
                        .getContentAsString(), BookDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save with invalid request dto")
    public void save_RequestDtoIsInvalid_ThrowsBadRequestException() throws Exception {
        CreateBookRequestDto invalidRequestDto = new CreateBookRequestDto(
                null,
                "Author Name 1",
                "978-0-00-000000-1",
                new BigDecimal("19.99"),
                "Description for Book 1",
                "https://coverimage1.jpg",
                List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);

        mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Save all
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save all with valid request dtos")
    public void saveAll_RequestDtosAreValid_ReturnsSavedBookList() throws Exception {
        CreateBookRequestDto[] requestDtos = {
                new CreateBookRequestDto(
                        "Book Title 3",
                        "Author Name 3",
                        "978-0-00-000000-3",
                        new BigDecimal("39.99"),
                        "Description for Book 3",
                        "https://coverimage3.jpg",
                        List.of(1L)),
                new CreateBookRequestDto(
                        "Book Title 4",
                        "Author Name 4",
                        "978-0-00-000000-4",
                        new BigDecimal("49.99"),
                        "Description for Book 4",
                        "https://coverimage4.jpg",
                        List.of(1L))};
        List<BookDto> expected = List.of(
                new BookDto(
                        3L,
                        "Book Title 4",
                        "Author Name 4",
                        "978-0-00-000000-4",
                        new BigDecimal("49.99"),
                        "Description for Book 4",
                        "https://coverimage4.jpg",
                        Set.of(1L)),
                new BookDto(
                        4L,
                        "Book Title 4",
                        "Author Name 4",
                        "978-0-00-000000-4",
                        new BigDecimal("49.99"),
                        "Description for Book 4",
                        "https://coverimage4.jpg",
                        Set.of(1L)));

        String jsonRequest = objectMapper.writeValueAsString(requestDtos);
        System.out.println(jsonRequest);

        MvcResult result = mockMvc.perform(post("/books/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<BookDto>>() {});

        assertEquals(expected.size(), actual.size());
        for (int i = 0; i < actual.size(); i++) {
            EqualsBuilder.reflectionEquals(expected.get(i), actual.get(i));
        }
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Save all with invalid request dtos throws BadRequestException")
    public void saveAll_RequestDtosAreInvalid_ThrowsBadRequestException() throws Exception {
        CreateBookRequestDto[] invalidRequestDtos = {
                mock(CreateBookRequestDto.class),
                mock(CreateBookRequestDto.class)};

        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDtos);
        System.out.println(jsonRequest);

        mockMvc.perform(post("/books/all")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Test methods for updateById
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by ID with valid request dto returns updated BookDto")
    public void updateById_IdAndRequestDtoAreValid_ReturnsUpdatedBookDto() throws Exception {
        Long validId = 1L;
        CreateBookRequestDto validRequestDto = new CreateBookRequestDto(
                "Book title 11",
                "Book author 11",
                "978-0-00-000001-1",
                new BigDecimal("19.99"),
                "Book description 11",
                "https://coverimage11.jpg",
                List.of(1L));

        BookDto expected = new BookDto(
                validId,
                validRequestDto.title(),
                validRequestDto.author(),
                validRequestDto.isbn(),
                validRequestDto.price(),
                validRequestDto.description(),
                validRequestDto.coverImage(),
                new HashSet<>(validRequestDto.categoryIds()));

        String jsonRequest = objectMapper.writeValueAsString(validRequestDto);

        MvcResult result = mockMvc.perform(put("/books/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);

        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by ID with invalid request dto throws BadRequestException")
    public void updateById_IdIsValidAndRequestDtoIsInvalid_ThrowsBadRequestException() throws Exception {
        Long validId = 1L;
        CreateBookRequestDto invalidRequestDto = new CreateBookRequestDto(
                null,
                "",
                "123",
                new BigDecimal("-1"),
                "",
                "invalid-url",
                List.of());

        String jsonRequest = objectMapper.writeValueAsString(invalidRequestDto);

        mockMvc.perform(put("/books/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Update by ID with invalid ID throws NotFoundException")
    public void updateById_IdIsInvalid_ThrowsNotFoundException() throws Exception {
        Long invalidId = 999L;
        CreateBookRequestDto validRequestDto = new CreateBookRequestDto(
                "Book title 11",
                "Book author 11",
                "978-0-00-000001-1",
                new BigDecimal("19.99"),
                "Book description 11",
                "https://coverimage11.jpg",
                List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(validRequestDto);

        mockMvc.perform(put("/books/" + invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test methods for deleteById
    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Delete by ID when ID exists performs deletion and returns no content")
    public void deleteById_IdExists_PerformsDeletionAndReturnsNoContent() throws Exception {
        Long existingId = 1L;

        mockMvc.perform(delete("/books/" + existingId))
                .andExpect(status().isNoContent());
    }

    @Sql(scripts = "classpath:database/controller/book/01-prepare-db-for-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/book/02-clear-db-after-book-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", authorities = {"admin"})
    @Test
    @DisplayName("Delete by ID when ID does not exist throws NotFoundException")
    public void deleteById_IdDoesNotExist_ThrowsNotFoundException() throws Exception {
        Long nonExistingId = 999L;

        mockMvc.perform(delete("/books/" + nonExistingId))
                .andExpect(status().isNotFound());
    }
}