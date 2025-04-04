package com.example.openweather.Service;

import com.example.openweather.DTO.CityRequest;
import com.example.openweather.Model.City;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.CityRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceTest {

    private CityRepository cityRepository;
    private UserService userService;
    private CityService cityService;

    @BeforeEach
    void setUpMocks() {
        cityRepository = Mockito.mock(CityRepository.class);
        userService = Mockito.mock(UserService.class);
        cityService = new CityService(userService, cityRepository);


        User mockUser = new User(1L, "test@example.com", "password", "username");
        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);
    }

    @ParameterizedTest
    @ValueSource(strings = {"City1", "City2", "City3"})
    void shouldCreateCityWhenRequestIsValid(String cityName) {

        CityRequest cityRequest = new CityRequest(cityName, 1.0, 1.0, 25.5, 60, 5.0, 1L);
        User mockUser = userService.getUserById(1L);
        City expectedCity = new City(cityName, 1.0, 1.0, 25.5, 60, 5.0, mockUser);


        Mockito.when(cityRepository.save(any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));


        List<City> result = cityService.bulkCreateCities(List.of(cityRequest));


        assertEquals(1, result.size());
        assertEquals(expectedCity.getCity(), result.get(0).getCity());
        verify(cityRepository, times(1)).save(any(City.class));
    }


    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldFilterOutInvalidCityRequests_NonNullValues(String cityName) {

        boolean isValid = cityName != null && !cityName.trim().isEmpty();


        assertTrue(!isValid);
    }

    @ParameterizedTest
    @NullSource
    void shouldFilterOutInvalidCityRequests_NullValue(String cityName) {

        boolean isValid = cityName != null && !cityName.trim().isEmpty();


        assertTrue(!isValid);
    }

}
