package com.online_store.backend.api.user.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

import com.online_store.backend.api.auth.dto.request.AuthRequestDto;
import com.online_store.backend.api.auth.exception.UserAlreadyExistsException;
import com.online_store.backend.api.user.dto.response.UserResponseDto;
import com.online_store.backend.api.user.entities.Role;
import com.online_store.backend.api.user.entities.User;
import com.online_store.backend.api.user.repository.UserRepository;
import com.online_store.backend.api.user.utils.mapper.CreateUserMapper;
import com.online_store.backend.api.user.utils.mapper.GetUserMapper;
import com.online_store.backend.common.utils.CommonUtilsService;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
public class UserServiceTest {
    @Mock
    private CommonUtilsService commonUtilsService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CreateUserMapper createUserMapper;

    @Mock
    private GetUserMapper getUserMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private AuthRequestDto authRequestDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password")
                .role(Role.CUSTOMER)
                .build();

        authRequestDto = AuthRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .build();

        userResponseDto = UserResponseDto.builder()
                .email("test@example.com")
                .role(Role.CUSTOMER)
                .build();
    }

    @Test
    @DisplayName("getActiveUser should return the details of the current authenticated user")
    void getActiveUser_ShouldReturnCurrentUser_WhenUserIsAuthenticated() {
        when(commonUtilsService.getCurrentUser()).thenReturn(user);
        when(getUserMapper.userMapper(eq(user))).thenReturn(userResponseDto);

        UserResponseDto result = userService.getActiveUser();

        assertNotNull(result);
        assertEquals(userResponseDto.getEmail(), result.getEmail());
        assertEquals(userResponseDto.getRole(), result.getRole());
        verify(commonUtilsService, times(1)).getCurrentUser();
        verify(getUserMapper, times(1)).userMapper(user);
    }

    @Test
    @DisplayName("createUser should successfully save a new user when email does not exist")
    void createUser_ShouldSaveNewUser_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail(authRequestDto.getEmail())).thenReturn(Optional.empty());
        when(createUserMapper.userMapper(authRequestDto, Role.CUSTOMER)).thenReturn(user);

        userService.createUser(authRequestDto, Role.CUSTOMER);

        verify(userRepository).findByEmail(authRequestDto.getEmail());
        verify(createUserMapper).userMapper(authRequestDto, Role.CUSTOMER);
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("createUser should throw UserAlreadyExistsException when email already exists")
    void createUser_ShouldThrowException_WhenEmailExists() {
        when(userRepository.findByEmail(eq(authRequestDto.getEmail()))).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(authRequestDto, Role.CUSTOMER));

        verify(userRepository, times(1)).findByEmail(eq(authRequestDto.getEmail()));
        verify(createUserMapper, never()).userMapper(any(AuthRequestDto.class), any(Role.class));
        verify(userRepository, never()).save(any(User.class));
    }
}
