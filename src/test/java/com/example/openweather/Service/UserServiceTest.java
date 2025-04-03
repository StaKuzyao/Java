package com.example.openweather.Service;

import com.example.openweather.Model.User;
import com.example.openweather.DAO.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Test
    void shouldFindUserByIdWhenExists() {

        User mockUser = new User(1L, "test@example.com", "password", "username");
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));


        User result = userService.getUserById(1L);


        assertEquals(mockUser, result);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());


        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));


        verify(userRepository, times(1)).findById(1L);
    }
}
