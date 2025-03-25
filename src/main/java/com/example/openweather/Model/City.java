package com.example.openweather.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity

public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private double lat;
    private double lon;
    private double temperature;
    private int humidity;
    private double windSpeed;



    @ManyToOne
    @JsonIgnore
    private User user;


    public City() {

    }

    public City(String city, double lat, double lon, double temperature, int humidity, double windSpeed, User user) {
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.temperature = temperature;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.user = user;
    }

}
