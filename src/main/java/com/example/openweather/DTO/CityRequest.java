package com.example.openweather.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityRequest {
    private String cityName;
    private double lat;
    private double lon;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private Long userId;

}
