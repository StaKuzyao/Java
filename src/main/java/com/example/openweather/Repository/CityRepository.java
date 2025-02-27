package com.example.openweather.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.openweather.Entity.City;

public interface CityRepository extends JpaRepository<City, Long> {
    City findByCity(String city);
}
