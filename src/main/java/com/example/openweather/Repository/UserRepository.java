package com.example.openweather.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.openweather.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
