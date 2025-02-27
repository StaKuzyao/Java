package com.example.openweather.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.openweather.Entity.User;
import com.example.openweather.Entity.City;
import com.example.openweather.Repository.UserRepository;
import com.example.openweather.Repository.CityRepository;
import com.example.openweather.DTO.WeatherResponse;
import com.example.openweather.Model.WeatherDetails;
import java.util.List;



@Service
public class WeatherService {


    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "896cc0b7d076260bdbb9f61f6dd5c18e"; // Убедитесь, что у вас есть действующий ключ API

    @Autowired
    public WeatherService(UserRepository userRepository, CityRepository cityRepository) {
        this.userRepository = userRepository;
        this.cityRepository = cityRepository;
    }

    @Transactional
    public User createUser(Long id,String email, String password, String username) {
        User user = new User(id, email, password, username);
        return userRepository.save(user);
    }

    @Transactional
    public City createCity(String cityName, double lat, double lon, double temperature, int humidity,double windSpeed, Long userId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        City city = new City(cityName, lat, lon, temperature, humidity,windSpeed, user);
        return cityRepository.save(city);
    }

    @Transactional
    public void addCityToUser(Long userId, Long cityId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        City city = cityRepository.findById(cityId).orElseThrow(() -> new IllegalArgumentException("Invalid city ID"));
        city.setUser(user);
        cityRepository.save(city);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<City> getAllCities() {
        return cityRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
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
