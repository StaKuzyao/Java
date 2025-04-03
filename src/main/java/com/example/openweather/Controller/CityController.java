package com.example.openweather.Controller;

import com.example.openweather.Model.City;
import com.example.openweather.Service.CityService;
import com.example.openweather.DTO.CityRequest;
import com.example.openweather.DTO.WeatherResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/cities")
@Tag(name = "City Controller", description = "Управление городами и погодными данными")
public class CityController {

    private final CityService cityService;
    private static final Logger logger = LoggerFactory.getLogger(CityController.class);
    final int STATUS = 500;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/bulk-create")
    public ResponseEntity<List<City>> bulkCreateCities(@RequestBody List<CityRequest> cityRequests) {
        List<City> createdCities = cityService.bulkCreateCities(cityRequests);
        return ResponseEntity.ok(createdCities);
    }


    @PostMapping("/create")
    @Operation(summary = "Создать новый город", description = "Создаёт город с переданными параметрами")
    public ResponseEntity<City> createCity(
            @RequestParam String cityName,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double temperature,
            @RequestParam int humidity,
            @RequestParam double windSpeed,
            @RequestParam Long userId
    ) {
        City city = cityService.createCity(cityName, lat, lon, temperature, humidity, windSpeed, userId);
        return ResponseEntity.ok(city);
    }


    @Operation(summary = "Обновить данные города", description = "Обновляет информацию о городе по ID")
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(
            @PathVariable Long id,
            @RequestParam String cityName,
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double temperature,
            @RequestParam int humidity,
            @RequestParam double windSpeed,
            @RequestParam Long userId
    ) {
        City city = cityService.updateCity(id, cityName, lat, lon, temperature, humidity, windSpeed, userId);
        return ResponseEntity.ok(city);
    }


    @Operation(summary = "Удалить город", description = "Удаляет город из базы данных по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok("Город успешно удален.");
    }

    @Operation(summary = "Получить город по ID",
            description = "Возвращает информацию о городе по указанному ID")
    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @Operation(summary = "Получить все города",
            description = "Возвращает список всех городов")
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @Operation(summary = "Получить погоду по названию города",
            description = "Возвращает данные о погоде для указанного города")
    @GetMapping("/weather")
    public ResponseEntity<?> getWeatherByCity(@RequestParam String city) {
        try {
            WeatherResponse weatherResponse = cityService.getWeatherByCity(city);
            return ResponseEntity.ok(weatherResponse);
        } catch (RuntimeException e) {
            logger.error("Ошибка при получении данных о погоде для города {}: {}", city, e.getMessage());
            return ResponseEntity.status(STATUS).body("Не удалось получить данные о погоде для города: "
                    + city);
        }
    }

    @Operation(summary = "Получить погоду по координатам",
            description = "Возвращает данные о погоде для указанных координат")
    @GetMapping("/weather/coord")
    public ResponseEntity<?> getWeatherByCoords(@RequestParam double lat, @RequestParam double lon) {
        WeatherResponse weatherResponse = cityService.getWeatherByCoords(lat, lon);
        return ResponseEntity.ok(weatherResponse);
    }

    @Operation(summary = "Получить города пользователя",
            description = "Возвращает список городов, связанных с указанным именем пользователя")
    @GetMapping("/by-username")
    public ResponseEntity<List<City>> getCitiesByUsername(@RequestParam String username) {
        List<City> cities = cityService.getCitiesByUsername(username);
        return ResponseEntity.ok(cities);
    }




}
