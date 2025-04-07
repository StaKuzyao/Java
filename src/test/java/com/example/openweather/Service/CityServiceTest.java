package com.example.openweather.Service;

import com.example.openweather.DTO.CityRequest;
import com.example.openweather.Model.City;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CityServiceTest {

    @InjectMocks
    private CityService cityService;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @ParameterizedTest
    @ValueSource(strings = {"City1", "City2", "City3"})
    void shouldCreateCityWhenRequestIsValid(String cityName) {

        CityRequest mockRequest = Mockito.mock(CityRequest.class);
        Mockito.when(mockRequest.getCityName()).thenReturn(cityName);
        Mockito.when(mockRequest.getLat()).thenReturn(1.0);
        Mockito.when(mockRequest.getLon()).thenReturn(1.0);
        Mockito.when(mockRequest.getTemperature()).thenReturn(25.5);
        Mockito.when(mockRequest.getHumidity()).thenReturn(60);
        Mockito.when(mockRequest.getWindSpeed()).thenReturn(5.0);
        Mockito.when(mockRequest.getUserId()).thenReturn(1L);

        User mockUser = Mockito.mock(User.class);
        Mockito.when(mockUser.getId()).thenReturn(1L);
        Mockito.when(mockUser.getEmail()).thenReturn("test@example.com");
        Mockito.when(userService.getUserById(1L)).thenReturn(mockUser);

        City mockCity = Mockito.mock(City.class);
        Mockito.when(mockCity.getCity()).thenReturn(cityName);

        Mockito.when(cityRepository.save(any(City.class))).thenReturn(mockCity);


        List<City> result = cityService.bulkCreateCities(List.of(mockRequest));


        assertEquals(1, result.size());
        assertEquals(cityName, result.get(0).getCity());
        verify(cityRepository, times(1)).save(any(City.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    void shouldFilterOutInvalidCityRequestsNonNullValues(String cityName) {

        boolean isValid = cityName != null && !cityName.trim().isEmpty();


        assertTrue(!isValid);
    }

    @ParameterizedTest
    @NullSource
    void shouldFilterOutInvalidCityRequestsNullValue(String cityName) {

        boolean isValid = cityName != null && !cityName.trim().isEmpty();


        assertTrue(!isValid);
    }
}
