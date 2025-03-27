package com.userdriverservice.controller;

import com.userdriverservice.dto.UserDto;
import com.userdriverservice.exception.UserNotFoundException;
import com.userdriverservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Контроллер для управления пользователями.
 * Предоставляет REST API для операций CRUD с пользователями.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    /**
     * Создает нового пользователя.
     *
     * @param userDto DTO с данными пользователя
     * @return ResponseEntity с созданным UserDto и статусом 201 (Created)
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        LOG.info("Creating user: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        LOG.info("User created: {}", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
    /**
     * Получает пользователя по идентификатору.
     *
     * @param id UUID пользователя
     * @return ResponseEntity с UserDto и статусом 200 (OK), если найден,
     *         или статусом 404 (Not Found), если не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        LOG.info("Getting user by id: {}", id);
        return userService.getUserById(id)
                .map(userDto -> {
                    LOG.info("User found: {}", userDto);
                    return ResponseEntity.ok(userDto);
                })
                .orElseGet(() -> {
                    LOG.warn("User not found with id: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
    /**
     * Получает список всех пользователей.
     *
     * @return ResponseEntity со списком UserDto и статусом 200 (OK),
     *         или статусом 204 (No Content), если пользователи не найдены
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        LOG.info("Getting all users");
        List<UserDto> userDtoList = userService.getAllUsers();
        if (userDtoList.isEmpty()) {
            LOG.info("No users found");
            return ResponseEntity.noContent().build();
        }
        LOG.info("Found {} users", userDtoList.size());
        return ResponseEntity.ok(userDtoList);
    }
    /**
     * Обновляет данные пользователя.
     *
     * @param id UUID пользователя
     * @param userDto DTO с обновленными данными
     * @return ResponseEntity с обновленным UserDto и статусом 200 (OK),
     *         или статусом 404 (Not Found), если пользователь не найден,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody UserDto userDto) {
        LOG.info("Updating user with id: {}, data: {}", id, userDto);
        try {
            UserDto updatedUser = userService.updateUser(id, userDto);
            LOG.info("User updated: {}", updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (UserNotFoundException e) {
            LOG.warn("Cannot update user, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error updating user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Удаляет пользователя.
     *
     * @param id UUID пользователя
     * @return ResponseEntity со статусом 204 (No Content) при успешном удалении,
     *         или статусом 404 (Not Found), если пользователь не найден,
     *         или статусом 500 (Internal Server Error) при ошибке
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        LOG.info("Deleting user with id: {}", id);
        try {
            userService.deleteUser(id);
            LOG.info("User deleted with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException e) {
            LOG.warn("Cannot delete user, not found with id: {}", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            LOG.error("Error deleting user with id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
