package com.userdriverservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.userdriverservice.dto.UserDto;
import com.userdriverservice.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Создание пользователя
     *
     * @param userDto
     * @return REST ответ
     */
    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        return ResponseEntity.ok().body(createdUser);
    }

    /**
     * Поиск пользователя по номеру телефона
     *
     * @param phone
     * @return REST ответ
     */
    @GetMapping("/{phone}")
    public ResponseEntity<UserDto> getUserByPhone(@PathVariable String phone) {
        UserDto userDto = userService.getUserByPhone(phone);
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

    /**
     * Поиск всех пользователей в базе
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserDto> userDtoList = userService.getAllUsers();
        if (userDtoList.isEmpty()) {
            LOG.info("userDtoList if empty");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            LOG.info("userDtoList is {}", userDtoList);
            return ResponseEntity.status(HttpStatus.OK).body(userDtoList);
        }
    }
}
