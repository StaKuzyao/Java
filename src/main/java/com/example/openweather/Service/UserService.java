package com.example.openweather.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.UserRepository;
import java.util.List;

@Service
public class UserService {

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(Long id, String email, String password, String username) {
        User user = new User(id, email, password, username);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, String email, String password, String username) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void addCityToUser(Long userId, Long cityId) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        // Дополнительная логика может быть добавлена здесь при необходимости.
    }
}
