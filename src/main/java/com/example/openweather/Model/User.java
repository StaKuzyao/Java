package com.example.openweather.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "users")
@JsonIgnoreProperties("cities")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String password;
    private String username;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<City> cities = new HashSet<>();


    public User() {
    }


    public User(Long id, String email, String password, String username) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
    }
}
