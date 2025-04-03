package com.example.openweather.Service;

import com.example.openweather.DTO.CityRequest;
import com.example.openweather.Model.City;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.CityRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void shouldCreateCitiesWhenRequestsAreValid() {

        List<CityRequest> cityRequests = Arrays.asList(
                new CityRequest("City1", 1.0, 1.0, 25.5, 60, 5.0, 1L),
                new CityRequest("City2", 2.0, 2.0, 30.0, 50, 3.5, 1L)
        );

        List<City> expectedCities = Arrays.asList(
                new City("City1", 1.0, 1.0, 25.5, 60, 5.0, new User(1L, "test@example.com", "password", "username")),
                new City("City2", 2.0, 2.0, 30.0, 50, 3.5, new User(1L, "test@example.com", "password", "username"))
        );

        Mockito.when(cityRepository.save(Mockito.any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));


        List<City> result = cityService.bulkCreateCities(cityRequests);


        assertEquals(expectedCities.size(), result.size());
        verify(cityRepository, times(2)).save(Mockito.any(City.class));
    }

    @Test
    void shouldFilterOutInvalidCityRequests() {

        List<CityRequest> cityRequests = Arrays.asList(
                new CityRequest(null, 0.0, 0.0, 0.0, 0, 0.0, 1L),
                new CityRequest("", 0.0, 0.0, 0.0, 0, 0.0, null),
                new CityRequest("City2", 2.0, 2.0, 30.0, 50, 3.5, 1L)
        );

        Mockito.when(cityRepository.save(Mockito.any(City.class))).thenAnswer(invocation -> invocation.getArgument(0));


        List<City> result = cityService.bulkCreateCities(cityRequests);


        assertEquals(1, result.size());
        verify(cityRepository, times(1)).save(Mockito.any(City.class));
    }
}
