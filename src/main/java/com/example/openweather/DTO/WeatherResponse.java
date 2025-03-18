package com.example.openweather.DTO;

import com.example.openweather.Model.WeatherDetails;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WeatherResponse {
    private String city;
    private WeatherDetails weatherDetails;



    public WeatherResponse(String city, WeatherDetails weatherDetails) {
        this.city = city;
        this.weatherDetails = weatherDetails;
    }

}
