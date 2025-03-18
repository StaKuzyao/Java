package com.example.openweather.Model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeatherDetails {
    private double temperature;
    private int humidity;
    private double windSpeed;


    public WeatherDetails() {
    }

    public WeatherDetails(double temperature, int humidity, double windSpeed) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
    }

}
