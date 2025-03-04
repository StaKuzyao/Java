package com.example.openweather.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.openweather.Model.City;

public interface CityRepository extends JpaRepository<City, Long> {
    City findByCity(String city);
}
