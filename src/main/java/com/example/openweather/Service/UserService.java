package com.example.openweather.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import com.example.openweather.Model.User;
import com.example.openweather.DAO.UserRepository;

import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @PersistenceContext
    private EntityManager entityManager;
    private final UserRepository userRepository;
    private final CacheService cacheService;

    @Autowired
    public UserService(UserRepository userRepository, CacheService cacheService) {
        this.userRepository = userRepository;
        this.cacheService = cacheService;
    }

    @Transactional
    public User createUser(Long id, String email, String password, String username) {
        logger.info("Создание пользователя с ID: {}, Email: {}", id, email);
        User user = new User(id, email, password, username);
        User savedUser = userRepository.save(user);
        logger.info("Пользователь успешно создан: {}", savedUser);
        cacheService.addToCache("user_" + savedUser.getId(), savedUser);
        return savedUser;
    }

    @Transactional
    public User updateUser(Long id, String email, String password, String username) {
        logger.info("Обновление пользователя с ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            logger.warn("Пользователь с ID: {} не найден", id);
            return new IllegalArgumentException("Invalid user ID");
        });
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        User updatedUser = userRepository.save(user);
        logger.info("Пользователь успешно обновлён: {}", updatedUser);
        cacheService.addToCache("user_" + updatedUser.getId(), updatedUser);
        return updatedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        logger.info("Проверяем существование пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            logger.warn("Пользователь с ID: {} не найден", id);
            throw new IllegalArgumentException("Пользователь с таким ID не существует.");
        }
        userRepository.deleteById(id);
        logger.info("Пользователь с ID: {} успешно удалён.", id);
    }


    public List<User> getAllUsers() {
        logger.info("Получение всех пользователей");


        logger.info("Текущее содержимое кэша:");
        for (Map.Entry<String, Object> entry : cacheService.getCache().entrySet()) {
            logger.info("Ключ: {}, Значение: {}", entry.getKey(), entry.getValue());
        }

        List<User> users = userRepository.findAll();
        logger.info("Найдено пользователей: {}", users.size());
        return users;
    }

    @Transactional
    public void addCityToUser(Long userId, Long cityId) {
        logger.info("Добавление города с ID: {} к пользователю с ID: {}", cityId, userId);
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            logger.warn("Пользователь с ID: {} не найден", userId);
            throw new IllegalArgumentException("Invalid user ID");
        }
        logger.info("Город с ID: {} успешно добавлен пользователю с ID: {}", cityId, userId);
    }

    public User getUserById(Long id) {
        String cacheKey = "user_" + id;
        logger.info("Получение пользователя с ID: {}", id);


        User cachedUser = (User) cacheService.getFromCache(cacheKey);
        if (cachedUser != null) {
            logger.info("Пользователь с ID: {} получен из кэша", id);
            return cachedUser;
        }

        // Если в кэше нет, загружаем из базы данных
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            logger.warn("Пользователь с ID: {} не найден", id);
        } else {
            logger.info("Пользователь с ID: {} найден: {}", id, user);
            cacheService.addToCache(cacheKey, user);
        }

        return user;
    }

    public void printCacheContents() {
        logger.info("Содержимое кэша:");
        for (Map.Entry<String, Object> entry : cacheService.getCache().entrySet()) {
            logger.info("Ключ: {}, Значение: {}", entry.getKey(), entry.getValue());
        }
    }
}
