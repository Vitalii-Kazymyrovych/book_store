package com.project.bookstore.service;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.project.bookstore.dto.category.CategoryDto;
import com.project.bookstore.dto.category.CreateCategoryRequestDto;
import com.project.bookstore.exception.EntityNotFoundException;
import com.project.bookstore.mapper.CategoryMapper;
import com.project.bookstore.model.Category;
import com.project.bookstore.repository.category.CategoryRepository;
import com.project.bookstore.service.category.CategoryServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Save with valid requestDto")
    void save_ValidRequestDto_ReturnsCategoryDto() {
        // Given
        CreateCategoryRequestDto requestDto = mock(CreateCategoryRequestDto.class);
        Category category = mock(Category.class);
        CategoryDto expected = mock(CategoryDto.class);
        when(categoryMapper.toModel(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);
        // When
        CategoryDto actual = categoryService.save(requestDto);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save all with valid requestDto list")
    void saveAll_ValidRequestDtos_ReturnsListOfCategoryDto() {
        // Given
        List<Category> categories = List.of(mock(Category.class), mock(Category.class));
        CategoryDto categoryDto = mock(CategoryDto.class);
        List<CategoryDto> expected = List.of(categoryDto, categoryDto);
        when(categoryRepository.saveAll(anyList())).thenReturn(categories);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        // When
        List<CategoryDto> actual = categoryService.saveAll(anyList());

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Save all with empty requestDto list")
    void saveAll_EmptyRequestDtos_ReturnsEmptyList() {
        // Given
        List<CreateCategoryRequestDto> requestDtos = List.of();

        // When
        List<CategoryDto> actualDtos = categoryService.saveAll(requestDtos);

        // Then
        assertTrue(actualDtos.isEmpty());
    }

    @Test
    @DisplayName("Save all with null")
    void saveAll_NullRequestDtos_ThrowsNullPointerException() {
        // Given
        List<CreateCategoryRequestDto> requestDtos = null;
        String expected = "Cannot invoke \"java.util.List.stream()"
                + "\" because \"requestDtos\" is null";
        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> categoryService.saveAll(requestDtos));
        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find all with valid data")
    void findAll_PageableParameter_ReturnsPaginatedCategoryDtos() {
        // Given
        Pageable pageable = mock(Pageable.class);
        List<Category> categories = List.of(mock(Category.class), mock(Category.class));
        Page<Category> pagedCategories
                = new PageImpl<>(categories, pageable, categories.size());
        CategoryDto categoryDto = mock(CategoryDto.class);
        List<CategoryDto> expected = List.of(categoryDto, categoryDto);
        when(categoryRepository.findAll(pageable)).thenReturn(pagedCategories);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(categoryDto);

        // When
        List<CategoryDto> actual = categoryService.findAll(pageable);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find all with null pageable")
    void findAll_NullPageable_ThrowsNullPointerException() {
        // Given
        Pageable pageable = null;
        String expected = "Cannot invoke \"org.springframework."
                + "data.domain.Page.stream()\" because the "
                + "return value of \"com.project.bookstore."
                + "repository.category.CategoryRepository.findAll"
                + "(org.springframework.data.domain.Pageable)\" is null";
        // When
        Exception actual = assertThrows(NullPointerException.class,
                () -> categoryService.findAll(pageable));
        //  Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find by id with valid id")
    void findById_ValidId_ReturnsCategoryDto() {
        // Given
        Category category = mock(Category.class);
        CategoryDto expectedDto = mock(CategoryDto.class);
        when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        // When
        CategoryDto actualDto = categoryService.findById(anyLong());

        // Then
        assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Find by id with invalid id")
    void findById_InvalidId_ThrowsEntityNotFoundException() {
        // Given
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.empty());
        String expected = "Can't find entity by id: 0";

        // When
        Exception actual = assertThrows(EntityNotFoundException.class, () -> categoryService.findById(anyLong()));

        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Find by id with null id")
    void findById_NullId_ThrowsEntityNotFoundException() {
        // Given
        Long nullId = null;
        String expected = "Can't find entity by id: null";
        // When
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.findById(nullId));
        // Then
        assertEquals(expected, actual.getMessage());
    }

    @Test
    @DisplayName("Update with valid id and requestDto")
    void update_ValidIdAndRequestDto_ReturnsUpdatedCategoryDto() {
        // Given
        Long validId = 1L;
        CreateCategoryRequestDto requestDto = mock(CreateCategoryRequestDto.class);
        Category categoryToUpdate = mock(Category.class);
        CategoryDto expected = mock(CategoryDto.class);
        when(categoryRepository.existsById(anyLong())).thenReturn(true);
        when(categoryMapper.toModel(requestDto)).thenReturn(categoryToUpdate);
        when(categoryRepository.save(categoryToUpdate)).thenReturn(categoryToUpdate);
        when(categoryMapper.toDto(categoryToUpdate)).thenReturn(expected);

        // When
        CategoryDto actual = categoryService.update(validId, requestDto);

        // Then
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update with invalid id")
    void update_InvalidId_ThrowsNullPointerException() {
        // Given
        CreateCategoryRequestDto requestDto
                = mock(CreateCategoryRequestDto.class);
        // When & Then
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(
                        anyLong(),
                        requestDto));
    }

    @Test
    @DisplayName("Update with null id")
    void update_NullId_ThrowsIllegalArgumentException() {
        // Given
        Long nullId = null;
        // When & Then
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(nullId, any(CreateCategoryRequestDto.class)));
    }

    @Test
    @DisplayName("Update with null requestDto")
    void update_NullRequestDto_ThrowsNullPointerException() {
        // Given
        CreateCategoryRequestDto nullRequestDto = null;
        // When & Then
        Exception actual = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(anyLong(), nullRequestDto));
    }

    @Test
    @DisplayName("Delete by id with valid id")
    void deleteById_ValidId_DeletesCategory() {
        // Given
        Long validId = 1L;
        doNothing().when(categoryRepository).deleteById(validId);

        // When
        categoryService.deleteById(validId);

        // Then
        verify(categoryRepository, times(1))
                .deleteById(validId);
    }
}