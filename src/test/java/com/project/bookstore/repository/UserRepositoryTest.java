package com.project.bookstore.repository;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import com.project.bookstore.model.User;
import com.project.bookstore.repository.user.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:database/repository/user/01-prepare-db-for-user-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/repository/user/02-clear-db-after-user-repository-test.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    // Find by email
    @Test
    @DisplayName("Find by email with valid email")
    public void findByEmail_ValiEmail_OptionalWithUser() {
        // Given
        String validEmail = "creativeName789@examples.org";
        Optional<User> expected = userRepository.findById(1L);
        // When
        Optional<User> actual = userRepository.findByEmail(validEmail);
        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find by email with invalid email")
    public void findByEmail_InvalidEmail_OptionalEmpty() {
        // Given
        String invalidEmail = "invalid email";
        Optional<User> expected = Optional.empty();

        // When
        Optional<User> actual = userRepository.findByEmail(invalidEmail);

        // Then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Find by email with null email")
    public void findByEmail_NullEmail_IllegalArgumentException() {
        // Given
        String nullEmail = null;
        Optional<User> expected = Optional.empty();

        // When
        Optional<User> actual = userRepository.findByEmail(nullEmail);

        // Then
        assertEquals(expected, actual);
    }
}