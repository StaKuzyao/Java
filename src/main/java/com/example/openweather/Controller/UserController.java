package com.example.openweather.Controller;

import com.example.openweather.Model.User;
import com.example.openweather.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Создать пользователя", description = "Добавляет нового пользователя")
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestParam Long id,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String username
    ) {
        User user = userService.createUser(id, email, password, username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Обновить пользователя", description = "Обновляет информацию о пользователе по ID")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String username
    ) {
        User user = userService.updateUser(id, email, password, username);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(
            @PathVariable Long id
    ) {
        try {
            logger.info("Запрос на удаление пользователя с ID: {}", id);
            userService.deleteUser(id);
            return ResponseEntity.ok("Пользователь успешно удалён");
        } catch (IllegalArgumentException e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Неожиданная ошибка: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body("Ошибка на сервере: " + e.getMessage());
        }
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о пользователе")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Добавить город к пользователю", description = "Привязывает город к пользователю по ID")
    @PostMapping("/{userId}/cities/{cityId}")
    public ResponseEntity<?> addCityToUser(@PathVariable Long userId, @PathVariable Long cityId) {
        userService.addCityToUser(userId, cityId);
        return ResponseEntity.ok("Город успешно добавлен пользователю.");
    }
}
