package com.example.openweather.Service;

import com.example.openweather.Model.User;
import com.example.openweather.DAO.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CacheService cacheService;

    @Mock
    private RequestCounterService requestCounterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L})
    void shouldFindUserByIdWhenExists(Long userId) {
        User mockUser = mock(User.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        User result = userService.getUserById(userId);

        assertEquals(mockUser, result);
        verify(userRepository, times(1)).findById(userId);
    }

    @ParameterizedTest
    @ValueSource(longs = {10L, 20L, 30L})
    void shouldThrowExceptionWhenUserDoesNotExist(Long userId) {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(userId));

        verify(userRepository, times(1)).findById(userId);
    }
}
