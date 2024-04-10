package com.project.bookstore.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookstore.dto.order.CreateOrderRequestDto;
import com.project.bookstore.dto.order.OrderDto;
import com.project.bookstore.dto.order.OrderWithoutItemsDto;
import com.project.bookstore.dto.order.UpdateOrderStatusDto;
import com.project.bookstore.dto.order.item.OrderItemDto;
import jakarta.servlet.ServletException;
import java.math.BigDecimal;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(MockitoExtension.class)
@Sql(scripts = "classpath:database/controller/order/" +
        "01-prepare-db-for-order-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/controller/order/" +
        "02-clear-db-after-order-controller-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class OrderControllerTest {
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
    @WithMockUser(
            username = "anotherRandomUser@domain.com",
            authorities = {"user"})
    @Test
    @DisplayName("Find all with valid authentication")
    public void findAll_ValidAuthentication_ReturnsOrderDtoList() throws Exception {
        // Given
        List<OrderDto> expected = List.of(
                new OrderDto(
                        1L,
                        1L,
                        List.of(),
                        "2024-03-29T15:44:10",
                        new BigDecimal("100.00"),
                        "NEW"),
                new OrderDto(2L,
                        1L,
                        List.of(),
                        "2024-03-29T15:44:10",
                        new BigDecimal("150.00"),
                        "NEW")
        );
        // When
        MvcResult result = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<OrderDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<OrderDto>>() {}
        );
        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "orderItemList");
    }

    @Test
    @DisplayName("Find all with unauthorized user")
    public void findAll_UnauthorizedUser_ThrowsException() throws Exception {
        // Given & When & Then
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Tests for save
    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("Save with valid authentication and request dto")
    public void save_ValidAuthenticationAndRequestDto_ReturnsOrderDto() throws Exception {
        // Given
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto("new address");
        OrderDto expected = new OrderDto(
                3L,
                1L,
                List.of(),
                "2024-03-29T15:44:10",
                new BigDecimal("799.70"),
                "NEW");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When
        MvcResult result = mockMvc.perform(post("/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        OrderDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), OrderDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual, "orderItemList");
    }

    @Test
    @DisplayName("Save with unauthorized user")
    public void save_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        CreateOrderRequestDto requestDto = new CreateOrderRequestDto("address");
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        // When & Then
        mockMvc.perform(post("/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("Save with null request dto")
    public void save_NullRequestDto_ThrowsException() throws Exception {
        // Given
        String jsonRequest = objectMapper.writeValueAsString(null);
        // When & Then
        mockMvc.perform(post("/orders")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    // Tests for getItems
    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("Get items with valid order ID and authentication")
    public void getItems_ValidOrderIdAndAuthentication_ReturnsOrderItemDtoList() throws Exception {
        // Given
        Long validOrderId = 1L;
        List<OrderItemDto> expected = List.of(
                new OrderItemDto (1L, 1L, 2),
                new OrderItemDto(2L, 2L, 1)
        );
        // When
        MvcResult result = mockMvc.perform(get("/orders/" + validOrderId + "/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<OrderItemDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<OrderItemDto>>() {}
        );
        // Then
        for (OrderItemDto expectedDto : expected) {
            for (OrderItemDto actualDto: actual) {
                if (expectedDto.id() == actualDto.id()) {
                    assertEquals(expectedDto, actualDto);
                }
            }
        }
    }

    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("Get items with invalid order ID")
    public void getItems_InvalidOrderId_ThrowsException() throws Exception {
        // Given
        Long invalidOrderId = 999L;
        // When
        mockMvc.perform(get("/orders/" + invalidOrderId + "/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // Test for unauthorized user
    @Test
    @DisplayName("GetItems with Unauthorized User Throws Exception")
    public void getItems_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long orderId = 1L; // Example order ID
        // When & Then
        mockMvc.perform(get("/orders/" + orderId + "/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    // Tests for findOrderItemById
    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("FindOrderItemById with Valid OrderId, ItemId, and Authentication Returns OrderItemDto")
    public void findOrderItemById_ValidOrderIdItemIdAndAuthentication_ReturnsOrderItemDto() throws Exception {
        // Given
        Long orderId = 1L;
        Long itemId = 1L;
        OrderItemDto expected = new OrderItemDto(1L, 1L, 2);
        String jsonResponse = objectMapper.writeValueAsString(expected);
        // When
        MvcResult result = mockMvc.perform(get("/orders/" + orderId + "/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        OrderItemDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), OrderItemDto.class);
        // Then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    // Test for invalid orderId
    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("FindOrderItemById with Invalid OrderId Throws Exception")
    public void findOrderItemById_InvalidOrderId_ThrowsException() throws Exception {
        // Given
        Long invalidOrderId = 999L; // Example invalid order ID
        Long itemId = 1L; // Example item ID
        // When & Then
        mockMvc.perform(get("/orders/" + invalidOrderId + "/items/" + itemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "anotherRandomUser@domain.com", authorities = {"user"})
    @Test
    @DisplayName("Find order item by ID with invalid item ID throws exception")
    public void findOrderItemById_InvalidItemId_ThrowsException() throws Exception {
        // Given
        Long orderID = 1L;
        Long invalidItemId = 999L;
        // When & Then
        assertThrows(ServletException.class, ()
                -> mockMvc.perform(get(
                        "/orders/"
                        + orderID
                        + "/items/"
                        + invalidItemId))
                .andReturn());
    }

    // Test for unauthorized user
    @Test
    @DisplayName("Find order item by ID with unauthorized user throws exception")
    public void findOrderItemById_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long orderId = 1L;
        Long itemId = 1L;
        // When & Then
        mockMvc.perform(get("/orders/" + orderId + "/items/" + itemId))
                .andExpect(status().isUnauthorized());
    }

    // Tests for updateStatus
    @Test
    @DisplayName("Update status with valid order ID and update DTO returns OrderWithoutItemsDto")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateStatus_ValidOrderIdAndUpdateDto_ReturnsOrderWithoutItemsDto() throws Exception {
        // Given
        Long validOrderId = 1L;
        UpdateOrderStatusDto updateDto = new UpdateOrderStatusDto("PAID");
        OrderWithoutItemsDto expected = new OrderWithoutItemsDto(
                validOrderId,
                1L,
                "2024-03-29T15:44:10",
                new BigDecimal("100.00"),
                "PAID");
        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        // When
        MvcResult result = mockMvc.perform(put("/orders/{orderId}", validOrderId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isOk())
                .andReturn();
        OrderWithoutItemsDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), OrderWithoutItemsDto.class);
        // Then
        assertEquals(expected, actual);
    }

    // Test for invalid order ID
    @Test
    @DisplayName("Update status with invalid order ID throws exception")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateStatus_InvalidOrderId_ThrowsException() throws Exception {
        // Given
        Long invalidOrderId = 999L;
        UpdateOrderStatusDto updateDto = new UpdateOrderStatusDto("PAID");
        String jsonRequest = objectMapper.writeValueAsString(updateDto);
        // When & Then
        mockMvc.perform(put("/orders/" + invalidOrderId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update status with null update DTO throws exception")
    @WithMockUser(username = "admin", authorities = {"admin"})
    public void updateStatus_NullUpdateDto_ThrowsException() throws Exception {
        // Given
        Long validOrderId = 1L;
        UpdateOrderStatusDto nullUpdateDto = null;
        // When & Then
        mockMvc.perform(put("/orders/" + validOrderId)
                        .content(objectMapper.writeValueAsString(nullUpdateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                
                .andExpect(status().isBadRequest());
    }

    // Test for unauthorized user
    @Test
    @DisplayName("Update status with unauthorized user throws exception")
    public void updateStatus_UnauthorizedUser_ThrowsException() throws Exception {
        // Given
        Long validOrderId = 1L;
        UpdateOrderStatusDto updateDto = new UpdateOrderStatusDto("NEW_STATUS");
        // When & Then
        mockMvc.perform(put("/orders/" + validOrderId)
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}