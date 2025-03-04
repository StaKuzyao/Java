package com.example.openweather.Controller;



import com.example.openweather.Model.City;
import com.example.openweather.Model.User;
import com.example.openweather.Service.WeatherService;
import com.example.openweather.DTO.WeatherResponse;
import com.example.openweather.DTO.CityRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather/city")
    public ResponseEntity<?> getWeatherByCity(@RequestParam String city) {
        WeatherResponse weatherResponse = weatherService.getWeatherByCity(city);
        return ResponseEntity.ok(weatherResponse);
    }

    @GetMapping("/weather/coord")
    public ResponseEntity<?> getWeatherByCoords(@RequestParam double lat, @RequestParam double lon) {
        WeatherResponse weatherResponse = weatherService.getWeatherByCoords(lat, lon);
        return ResponseEntity.ok(weatherResponse);
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestParam Long id, @RequestParam String email, @RequestParam String password, @RequestParam String username) {
        User user = weatherService.createUser(id, email, password, username);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestParam String email, @RequestParam String password, @RequestParam String username) {
        User user = weatherService.updateUser(id, email, password, username);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        weatherService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = weatherService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = weatherService.getAllUsers();
        return ResponseEntity.ok(users);
    }


    @PostMapping("/cities")
    public ResponseEntity<?> createCity(@RequestBody CityRequest cityRequest) {
        City city = weatherService.createCity(
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

    @PutMapping("/cities/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestParam String cityName, @RequestParam double lat, @RequestParam double lon, @RequestParam double temperature, @RequestParam int humidity, @RequestParam double windSpeed, @RequestParam Long userId) {
        City city = weatherService.updateCity(id, cityName, lat, lon, temperature, humidity, windSpeed, userId);
        return ResponseEntity.ok(city);
    }
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        weatherService.deleteCity(id);
        return ResponseEntity.ok("City deleted successfully.");
    }

    @GetMapping("/cities/{id}")
    public ResponseEntity<City> getCityById(@PathVariable Long id) {
        City city = weatherService.getCityById(id);
        return ResponseEntity.ok(city);
    }

    @GetMapping("/cities")
    public ResponseEntity<List<City>> getAllCities() {
        List<City> cities = weatherService.getAllCities();
        return ResponseEntity.ok(cities);
    }

    @PostMapping("/users/{userId}/cities/{cityId}")
    public ResponseEntity<?> addCityToUser(@PathVariable Long userId, @PathVariable Long cityId) {
        weatherService.addCityToUser(userId, cityId);
        return ResponseEntity.ok("City added to user successfully.");
    }

}
