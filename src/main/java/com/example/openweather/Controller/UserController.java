package com.example.openweather.Controller;

import com.example.openweather.Model.User;
import com.example.openweather.Service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/{userId}/cities/{cityId}")
    public ResponseEntity<?> addCityToUser(@PathVariable Long userId, @PathVariable Long cityId) {
        userService.addCityToUser(userId, cityId);
        return ResponseEntity.ok("City added to user successfully.");
    }
}
