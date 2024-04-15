package com.project.bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.shopping.cart.ShoppingCartDto;
import com.project.bookstore.dto.shopping.item.CartItemDto;
import com.project.bookstore.dto.shopping.item.CartItemWithoutBookTitleDto;
import com.project.bookstore.dto.shopping.item.CreateCartItemRequestDto;
import com.project.bookstore.dto.shopping.item.UpdateCartItemRequestDto;
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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "classpath:database/controller/shoppint-cart/" +
        "00-add-user-to-db-before-shopping-cart-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/controller/shoppint-cart/" +
        "03-delete-user-from-db-after-shopping-cart-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    // Tests for getCart
    @Test
    @DisplayName("Get cart with valid authentication returns ShoppingCartDto")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCart_ValidAuthentication_ReturnsShoppingCartDto() throws Exception {
        // Given
        CartItemDto item1 = new CartItemDto(
                1L,
                1L,
                "Book Title 1",
                10);
        CartItemDto item2 = new CartItemDto(
                2L,
                2L,
                "Book Title 2",
                20);
        List<CartItemDto> cartItems = List.of(item2, item1);
        ShoppingCartDto expected = new ShoppingCartDto(
                1L,
                1L,
                cartItems);
        // When
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        ShoppingCartDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        ShoppingCartDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "cartItems");
    }

    // Test for null authentication
    @Test
    @DisplayName("Get cart with unauthorized user throws exception")
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getCart_UnauthorizedUser_ThrowsException() throws Exception {
        // Given & When & Then
        mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Tests for postItem
    @Test
    @DisplayName("Post item with valid authentication and request DTO returns CartItemWithoutBookTitleDto")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postItem_ValidAuthenticationAndRequestDto_ReturnsCartItemWithoutBookTitleDto() throws Exception {
        // Given
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(1L, 20);
        CartItemWithoutBookTitleDto expected = new CartItemWithoutBookTitleDto(3L, 1L, 20);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemWithoutBookTitleDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(),
                        CartItemWithoutBookTitleDto.class);
        // Then
        assertEquals(expected, actual);
    }

    // Test for null authentication
    @Test
    @DisplayName("Post item with unauthorized user throws exception")
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postItem_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto(1L, 20);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/cart")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Test for null request DTO
    @Test
    @DisplayName("Post item with null request DTO throws exception")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void postItem_NullRequestDto_ThrowsException() throws Exception {
        // Given
        CreateCartItemRequestDto nullRequestDto = null;
        // When & Then
        mockMvc.perform(post("/cart")
                        .content(objectMapper.writeValueAsString(nullRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    // Tests for updateItem
    @Test
    @DisplayName("Update item with valid ID, authentication, and request DTO returns CartItemWithoutBookTitleDto")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItem_ValidIdAuthenticationAndRequestDto_ReturnsCartItemWithoutBookTitleDto() throws Exception {
        // Given
        Long validId = 1L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(30);
        CartItemWithoutBookTitleDto expected = new CartItemWithoutBookTitleDto(1L, 1L, 30);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(put("/cart/cart-items/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        CartItemWithoutBookTitleDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), CartItemWithoutBookTitleDto.class);
        // Then
        assertEquals(expected, actual);
    }

    // Test for invalid ID
    @Test
    @DisplayName("Update item with invalid ID throws exception")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItem_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = 999L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(30);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(put("/cart/cart-items/" + invalidId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update item with unauthorized user throws exception")
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItem_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        UpdateCartItemRequestDto requestDto = new UpdateCartItemRequestDto(30);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(put("/cart/cart-items/" + validId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Test for null request DTO
    @Test
    @DisplayName("Update item with null request DTO throws exception")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateItem_NullRequestDto_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        UpdateCartItemRequestDto nullRequestDto = null;

        // When & Then
        mockMvc.perform(put("/cart/cart-items/" + validId)
                        .content(objectMapper.writeValueAsString(nullRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Tests for deleteItem
    @Test
    @DisplayName("Delete item with valid ID and authentication performs deletion")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteItem_ValidIdAndAuthentication_PerformsDeletion() throws Exception {
        // Given
        Long validId = 1L;
        // When & Then
        mockMvc.perform(delete("/cart/cart-items/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Test for invalid ID
    @Test
    @DisplayName("Delete item with invalid ID throws exception")
    @WithMockUser(username = "penultimateUniqueUser@sample.net", authorities = {"user"})
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteItem_InvalidId_ThrowsException() throws Exception {
        // Given
        Long invalidId = 999L;
        // When & Then
        mockMvc.perform(delete("/cart-items/{id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test for null authentication
    @Test
    @DisplayName("Delete item with unauthorized user throws exception")
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "01-prepare-db-for-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/controller/shoppint-cart/" +
            "02-clear-db-after-shopping-cart-controller-test.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void deleteItem_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validId = 1L;
        // When & Then
        mockMvc.perform(delete("/cart/cart-items/" + validId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}