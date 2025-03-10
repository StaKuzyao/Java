package com.example.openweather.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.openweather.Model.City;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.CityRepository;
import com.example.openweather.DTO.WeatherResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.openweather.Model.WeatherDetails;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class CityService {

    @PersistenceContext
    private EntityManager entityManager;
    private final CityRepository cityRepository;

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "896cc0b7d076260bdbb9f61f6dd5c18e";

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public City createCity(String cityName, double lat, double lon, double temperature, int humidity, double windSpeed, Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        City city = new City(cityName, lat, lon, temperature, humidity, windSpeed, user);
        return cityRepository.save(city);
    }

    @Transactional
    public City updateCity(Long id, String cityName, double lat, double lon, double temperature, int humidity, double windSpeed, Long userId) {
        City city = cityRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid city ID"));
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        city.setCity(cityName);
        city.setLat(lat);
        city.setLon(lon);
        city.setTemperature(temperature);
        city.setHumidity(humidity);
        city.setWindSpeed(windSpeed);
        city.setUser(user);
        return cityRepository.save(city);
    }

    @Transactional
    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public City getCityById(Long cityId) {
        return cityRepository.findById(cityId).orElse(null);
    }

    public WeatherResponse getWeatherByCity(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = WEATHER_URL + "?q=" + city + "&appid=" + API_KEY;
        String result = restTemplate.getForObject(url, String.class);

        return mapToWeatherResponse(city, result);
    }

    public WeatherResponse getWeatherByCoords(double lat, double lon) {
        RestTemplate restTemplate = new RestTemplate();
        String url = WEATHER_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
        String result = restTemplate.getForObject(url, String.class);

        String city = extractCityFromJson(result);
        return mapToWeatherResponse(city, result);
    }

    private WeatherResponse mapToWeatherResponse(String city, String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode main = root.path("main");
            double temperature = main.path("temp").asDouble();
            int humidity = main.path("humidity").asInt();

            JsonNode wind = root.path("wind");
            double windSpeed = wind.path("speed").asDouble();

            WeatherDetails weatherDetails = new WeatherDetails(temperature, humidity, windSpeed);
            return new WeatherResponse(city, weatherDetails);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String extractCityFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            return root.path("name").asText();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
