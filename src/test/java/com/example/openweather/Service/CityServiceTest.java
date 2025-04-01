package com.example.openweather.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.example.openweather.DAO.CityRepository;
import com.example.openweather.Model.*;
import com.example.openweather.DTO.CityRequest;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CityServiceTest {

    private CityService cityService;
    private UserService userService;
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        cityRepository = Mockito.mock(CityRepository.class);
        cityService = new CityService(userService, cityRepository);

        User mockUser = new User(1L, "test@example.com", "password", "username");
        when(userService.getUserById(1L)).thenReturn(mockUser);
    }

    @Test
    void bulkCreateCities_shouldCreateCities() {
        List<CityRequest> cityRequests = Arrays.asList(
                new CityRequest("City1", 1.0, 1.0, 25.5, 60, 5.0, 1L),
                new CityRequest("City2", 2.0, 2.0, 30.0, 50, 3.5, 1L)
        );

        List<City> expectedCities = Arrays.asList(
                new City("City1", 1.0, 1.0, 25.5, 60, 5.0, new User(1L, "test@example.com", "password", "username")),
                new City("City2", 2.0, 2.0, 30.0, 50, 3.5, new User(1L, "test@example.com", "password", "username"))
        );

        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<City> result = cityService.bulkCreateCities(cityRequests);

        assertEquals(expectedCities.size(), result.size());
        verify(cityRepository, times(2)).save(any(City.class));
    }

    @Test
    void bulkCreateCities_shouldFilterInvalidRequests() {
        List<CityRequest> cityRequests = Arrays.asList(
                new CityRequest(null, 0.0, 0.0, 0.0, 0, 0.0, 1L), // Некорректный город
                new CityRequest("City2", 2.0, 2.0, 30.0, 50, 3.5, 1L)
        );

        when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<City> result = cityService.bulkCreateCities(cityRequests);

        assertEquals(1, result.size()); // Только один корректный город
        verify(cityRepository, times(1)).save(any(City.class));
    }
}
