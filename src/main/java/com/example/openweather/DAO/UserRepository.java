package com.example.openweather.DAO;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.openweather.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
