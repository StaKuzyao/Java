package com.example.openweather.Service;

import com.example.openweather.Model.User;
import com.example.openweather.DAO.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private CacheService cacheService;
    private UserService userService;
    private RequestCounterService requestCounterService;

    @BeforeEach
    void setUpMocks() {
        userRepository = Mockito.mock(UserRepository.class);
        cacheService = Mockito.mock(CacheService.class);
        requestCounterService = Mockito.mock(RequestCounterService.class);
        userService = new UserService(userRepository, cacheService, requestCounterService);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldFindUserByIdWhenExists(Long userId) {

        User mockUser = new User(userId, userId + "@example.com", "password", "username" + userId);
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));


        User result = userService.getUserById(userId);


        assertEquals(mockUser, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @ParameterizedTest
    @ValueSource(longs = {10L, 20L, 30L})
    void shouldThrowExceptionWhenUserDoesNotExist(Long userId) {

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> userService.getUserById(userId));


        verify(userRepository, times(1)).findById(userId);
    }
}
