package com.example.openweather.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityRequest {
    private String cityName;
    private double lat;
    private double lon;
    private double temperature;
    private int humidity;
    private double windSpeed;
    private Long userId;
    
}


