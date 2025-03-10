package com.example.openweather.Controller;

import com.example.openweather.Model.City;
import com.example.openweather.Service.CityService;
import com.example.openweather.DTO.CityRequest;
import com.example.openweather.DTO.WeatherResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {

    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping
    public ResponseEntity<?> createCity(@RequestBody CityRequest cityRequest) {
        City city = cityService.createCity(
                cityRequest.getCityName(),
                cityRequest.getLat(),
                cityRequest.getLon(),
                cityRequest.getTemperature(),
                cityRequest.getHumidity(),
                cityRequest.getWindSpeed(),
                cityRequest.getUserId()
        );
        return ResponseEntity.ok(city);
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        cityService.deleteCity(id);
        return ResponseEntity.ok("City deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = cityService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = cityService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @GetMapping("/weather")
    public ResponseEntity<?> getWeatherByCity(@RequestParam String city) {
        WeatherResponse weatherResponse = cityService.getWeatherByCity(city);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping("/weather/coord")
    public ResponseEntity<?> getWeatherByCoords(@RequestParam double lat, @RequestParam double lon) {
        WeatherResponse weatherResponse = cityService.getWeatherByCoords(lat, lon);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping("/by-username")
    public ResponseEntity<List<City>> getCitiesByUsername(@RequestParam String username) {
        List<City> cities = cityService.getCitiesByUsername(username);
        return ResponseEntity.ok(cities);
    }

}
