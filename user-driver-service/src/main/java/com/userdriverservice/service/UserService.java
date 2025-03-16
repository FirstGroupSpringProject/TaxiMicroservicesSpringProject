package com.userdriverservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.userdriverservice.dto.UserDto;
import com.userdriverservice.entity.User;
import com.userdriverservice.mapper.UserMapper;
import com.userdriverservice.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    public UserDto getUserByPhone(String phone) {
        User user = userRepository.findUserByPhone(phone);
        return userMapper.toDto(user);
    }

    public UserDto updateUser(UUID id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow();
        userMapper.updateUser(userDto, user); // Обновляем поля
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            LOG.info("No users found");
            return null;
        } else {
            return users.stream()
                    .map(userMapper::toDto)
                    .toList();
        }
    }
}
