package com.online_store.backend.api.user.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.online_store.backend.api.auth.exception.UserNotFoundException;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserUtilsService Unit Tests")
public class UserUtilsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserUtilsService userUtilsService;

    private User user;
    private final String existingEmail = "test@example.com";
    private final String nonExistingEmail = "nonexistent@example.com";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email(existingEmail)
                .password("password")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("findUserByEmail should return user when email exists")
    void findUserByEmail_ShouldReturnUser_WhenEmailExists() {
        when(userRepository.findByEmail(eq(existingEmail))).thenReturn(Optional.of(user));

        User foundUser = userUtilsService.findUserByEmail(existingEmail);

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getId(), foundUser.getId());
        verify(userRepository).findByEmail(existingEmail);
    }

    @Test
    @DisplayName("findUserByEmail should throw UserNotFoundException when email does not exist")
    void findUserByEmail_ShouldThrowException_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail(eq(nonExistingEmail))).thenReturn(Optional.empty());

       UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userUtilsService.findUserByEmail(nonExistingEmail));

        assertEquals("User not found with email:" + nonExistingEmail, exception.getMessage());
        verify(userRepository).findByEmail(eq(nonExistingEmail));
    }
}
