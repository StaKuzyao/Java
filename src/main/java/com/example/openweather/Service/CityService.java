package com.example.openweather.Service;

import com.example.openweather.DTO.CityRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.Map;
import java.util.stream.Collectors;


@Service
public class CityService {

    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    @PersistenceContext
    private EntityManager entityManager;
    private CityRepository cityRepository;
    private CacheService cacheService;
    private UserService userService;
    private final RequestCounterService requestCounterService;
    private CityService cityService;
    private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "***********************************";

    @Autowired
    public CityService(CityRepository cityRepository, CacheService cacheService, UserService userService, RequestCounterService requestCounterService) {
        this.cityRepository = cityRepository;
        this.cacheService = cacheService;
        this.userService = userService;
        this.requestCounterService = requestCounterService;
    }


    @Transactional
    public City createCity(String cityName, double lat, double lon,
                           double temperature, int humidity, double windSpeed, Long userId) {

        requestCounterService.increment();

        logger.info("Создание города: {}, Координаты: ({}, {}), Пользователь ID: {}",
                cityName, lat, lon, userId);
        logCacheContents();

        User user = entityManager.find(User.class, userId);
        if (user == null) {
            logger.warn("Пользователь с ID: {} не найден", userId);
            throw new IllegalArgumentException("Invalid user ID");
        }
        City city = new City(cityName, lat, lon, temperature, humidity, windSpeed, user);
        City savedCity = cityRepository.save(city);
        logger.info("Город успешно создан: {}", savedCity);
        cacheService.addToCache("city_" + savedCity.getId(), savedCity);
        return savedCity;
    }

    @Transactional
    public City updateCity(Long id, String cityName, double lat, double lon,
                           double temperature, int humidity, double windSpeed, Long userId) {

        requestCounterService.increment();

        logger.info("Обновление города с ID: {}", id);
        logCacheContents();

        City city = cityRepository.findById(id).orElseThrow(() -> {
            logger.warn("Город с ID: {} не найден", id);
            return new IllegalArgumentException("Invalid city ID");
        });
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            logger.warn("Пользователь с ID: {} не найден", userId);
            throw new IllegalArgumentException("Invalid user ID");
        }
        city.setCity(cityName);
        city.setLat(lat);
        city.setLon(lon);
        city.setTemperature(temperature);
        city.setHumidity(humidity);
        city.setWindSpeed(windSpeed);
        city.setUser(user);
        City updatedCity = cityRepository.save(city);
        logger.info("Город успешно обновлен: {}", updatedCity);
        cacheService.addToCache("city_" + updatedCity.getId(), updatedCity);
        return updatedCity;
    }

    @Transactional
    public void deleteCity(Long id) {

        requestCounterService.increment();

        logger.info("Проверяем существование города с ID: {}", id);
        if (!cityRepository.existsById(id)) {
            logger.warn("Город с ID: {} не найден", id);
            throw new IllegalArgumentException("Город с таким ID не существует.");
        }
        cityRepository.deleteById(id);
        logger.info("Город с ID: {} успешно удалён.", id);
    }

    public List<City> getAllCities() {

        requestCounterService.increment();

        logger.info("Получение всех городов");
        logCacheContents();

        List<City> cities = cityRepository.findAll();
        logger.info("Найдено городов: {}", cities.size());
        return cities;
    }

    public City getCityById(Long cityId) {

        requestCounterService.increment();

        logger.info("Получение города с ID: {}", cityId);
        logCacheContents();

        City cachedCity = (City) cacheService.getFromCache("city_" + cityId);
        if (cachedCity != null) {
            logger.info("Город с ID: {} получен из кэша", cityId);
            return cachedCity;
        }

        City city = cityRepository.findById(cityId).orElse(null);
        if (city == null) {
            logger.warn("Город с ID: {} не найден", cityId);
        } else {
            logger.info("Город найден: {}", city);
            cacheService.addToCache("city_" + cityId, city);
        }
        return city;
    }

    public WeatherResponse getWeatherByCity(String city) {

        requestCounterService.increment();

        logger.info("Получение погодных данных для города: {}", city);


        String url = String.format("%s?q=%s&appid=%s&units=metric", WEATHER_URL, city, API_KEY);

        try {

            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(url, String.class);
            logger.info("Успешный ответ от API: {}", response);


            return mapToWeatherResponse(response);

        } catch (Exception e) {
            logger.error("Ошибка при вызове API для города {}: {}", city, e.getMessage());
            throw new RuntimeException("Не удалось получить данные о погоде для города " +
                    city + ". Проверьте название города.");
        }
    }


    public WeatherResponse getWeatherByCoords(double lat, double lon) {

        requestCounterService.increment();

        logger.info("Получение погодных данных для координат: lat={}, lon={}", lat, lon);
        logCacheContents();

        RestTemplate restTemplate = new RestTemplate();
        String url = WEATHER_URL + "?lat=" + lat + "&lon=" + lon + "&appid=" + API_KEY;
        String result = restTemplate.getForObject(url, String.class);
        logger.info("Погодные данные получены: {}", result);

        String city = extractCityFromJson(result);
        return mapToWeatherResponse(city, result);
    }

    private WeatherResponse mapToWeatherResponse(String city, String json) {

        requestCounterService.increment();

        try {
            logger.info("Маппинг данных погоды для города: {}", city);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            JsonNode main = root.path("main");
            double temperature = main.path("temp").asDouble();
            int humidity = main.path("humidity").asInt();

            JsonNode wind = root.path("wind");
            double windSpeed = wind.path("speed").asDouble();

            WeatherDetails weatherDetails = new WeatherDetails(temperature, humidity, windSpeed);
            WeatherResponse response = new WeatherResponse(city, weatherDetails);
            logger.info("Погодный объект успешно создан: {}", response);
            return response;

        } catch (Exception e) {
            logger.error("Ошибка при обработке погодных данных: {}", e.getMessage(), e);
            return null;
        }
    }

    private String extractCityFromJson(String json) {

        requestCounterService.increment();

        try {
            logger.info("Извлечение названия города из JSON");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);
            String cityName = root.path("name").asText();
            logger.info("Название города извлечено: {}", cityName);
            return cityName;

        } catch (Exception e) {
            logger.error("Ошибка при извлечении названия города: {}", e.getMessage(), e);
            return null;
        }
    }

    public List<City> getCitiesByUsername(String username) {

        requestCounterService.increment();

        logger.info("Получение всех городов для пользователя с именем: {}", username);
        logCacheContents();

        List<City> cities = cityRepository.findCitiesByUsername(username);
        logger.info("Найдено городов для пользователя {}: {}", username, cities.size());
        return cities;
    }

    private void logCacheContents() {

        requestCounterService.increment();

        logger.info("Текущее содержимое кэша:");
        for (Map.Entry<String, Object> entry : cacheService.getCache().entrySet()) {
            logger.info("Ключ: {}, Значение: {}", entry.getKey(), entry.getValue());
        }
    }

    private WeatherResponse mapToWeatherResponse(String json) {

        requestCounterService.increment();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);


            String cityName = rootNode.path("name").asText();


            JsonNode mainNode = rootNode.path("main");
            double temperature = mainNode.path("temp").asDouble();
            int humidity = mainNode.path("humidity").asInt();


            JsonNode windNode = rootNode.path("wind");
            double windSpeed = windNode.path("speed").asDouble();


            WeatherDetails weatherDetails = new WeatherDetails(temperature, humidity, windSpeed);
            return new WeatherResponse(cityName, weatherDetails);

        } catch (Exception e) {
            logger.error("Ошибка при обработке ответа API: {}", e.getMessage());
            throw new RuntimeException("Не удалось обработать данные о погоде.");
        }
    }


    public List<City> bulkCreateCities(List<CityRequest> cityRequests) {

        requestCounterService.increment();

        return cityRequests.stream()
                .filter(request -> request.getCityName() != null && !request.getCityName().isEmpty())
                .map(request -> {
                    User user = userService.getUserById(request.getUserId());
                    return new City(
                            request.getCityName(),
                            request.getLat(),
                            request.getLon(),
                            request.getTemperature(),
                            request.getHumidity(),
                            request.getWindSpeed(),
                            user
                    );
                })
                .map(cityRepository::save)
                .collect(Collectors.toList());
    }


}