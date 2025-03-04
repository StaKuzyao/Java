package com.example.openweather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.openweather.DAO")
@EntityScan(basePackages = "com.example.openweather.Model")
public class OpenWeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenWeatherApplication.class, args);
    }
}
