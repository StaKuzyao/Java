package com.example.openweather.Controller;



import com.example.openweather.Entity.*;
import com.example.openweather.Service.WeatherService;
import com.example.openweather.DTO.WeatherResponse;
import com.example.openweather.DTO.CityRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

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

    @PostMapping("/users/{userId}/cities/{cityId}")
    public ResponseEntity<?> addCityToUser(@PathVariable Long userId, @PathVariable Long cityId) {
        weatherService.addCityToUser(userId, cityId);
        return ResponseEntity.ok("City added to user successfully.");
    }

}
