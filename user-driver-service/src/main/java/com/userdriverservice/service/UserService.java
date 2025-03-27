package com.userdriverservice.service;

import com.userdriverservice.dto.UserDto;
import com.userdriverservice.entity.User;
import com.userdriverservice.event.UserCreatedEvent;
import com.userdriverservice.exception.UserNotFoundException;
import com.userdriverservice.mapper.UserMapper;
import com.userdriverservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
/**
 * Сервис для работы с пользователями.
 * Предоставляет CRUD-операции и бизнес-логику для управления пользователями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private KafkaTemplate<String, UserCreatedEvent> userEventKafkaTemplate;
    private static final String USER_EVENTS_TOPIC = "user-events";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaTemplate<String, Object> kafkaTemplate;


    /**
     * Создает нового пользователя.
     *
     * @param userDto DTO с данными пользователя
     * @return созданный UserDto
     */
    @Transactional
    public UserDto createUser(UserDto userDto) {

        User user = userMapper.toEntity(userDto);
        User savedUser = userRepository.save(user);
        UserDto savedDto = userMapper.toDto(savedUser);
        log.info("User created with id: {}", savedUser.getId());

        UserCreatedEvent event = new UserCreatedEvent(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getPhone()
        );
        try {
            log.info("Sending UserCreatedEvent for userId: {}", savedUser.getId());
            userEventKafkaTemplate.send(USER_EVENTS_TOPIC, savedUser.getId().toString(), event);
        } catch (Exception e) {
            log.error("Failed to send UserCreatedEvent for userId: {}. Error: {}", savedUser.getId(), e.getMessage(), e);
        }

        return savedDto;
    }
    /**
     * Обновляет данные пользователя.
     *
     * @param id UUID пользователя
     * @param userDto DTO с обновленными данными
     * @return обновленный UserDto
     * @throws UserNotFoundException если пользователь не найден
     * @throws RuntimeException если телефонный номер уже существует
     */
    @Transactional
    public UserDto updateUser(UUID id, UserDto userDto) {
        log.info("Attempting to update user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("id", id.toString()));

        if (userDto.getPhone() != null && !userDto.getPhone().equals(user.getPhone())) {
            User existingByPhone = userRepository.findUserByPhone(userDto.getPhone());
            if (existingByPhone != null && !existingByPhone.getId().equals(id)) {
                log.warn("Attempt to update user {} with phone {} which already exists for user {}", id, userDto.getPhone(), existingByPhone.getId());
                throw new RuntimeException("Phone number already exists");
            }
        }

        userMapper.updateUser(userDto, user);
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully: {}", updatedUser.getId());
        return userMapper.toDto(updatedUser);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id UUID пользователя
     * @return Optional с UserDto, если пользователь найден
     */
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(UUID id) {
        log.debug("Fetching user by ID: {}", id);
        return userRepository.findById(id).map(userMapper::toDto);
    }

    /**
     * Получает пользователя по телефонному номеру.
     *
     * @param phone телефонный номер
     * @return UserDto пользователя или null, если не найден
     */
    @Transactional(readOnly = true)
    public UserDto getUserByPhone(String phone) {
        log.debug("Finding user by phone: {}", phone);
        User user = userRepository.findUserByPhone(phone);
        if (user == null) {
            log.warn("User not found with phone: {}", phone);
        }
        return userMapper.toDto(user);
    }

    /**
     * Удаляет пользователя.
     *
     * @param id UUID пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUser(UUID id) {
        log.info("Deleting user with ID: {}", id);
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userRepository.deleteById(id);
        log.info("User deleted: {}", id);

        sendUserEvent(userMapper.toDto(userToDelete), "DELETED");
    }
    /**
     * Получает список всех пользователей.
     *
     * @return список UserDto
     */
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        log.debug("Fetching all users");
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }


    /**
     * Отправляет событие о пользователе в Kafka.
     *
     * @param userDto DTO пользователя
     * @param eventType тип события ("CREATED", "UPDATED", "DELETED")
     */
    private void sendUserEvent(UserDto userDto, String eventType) {
        try {
            Map<String, Object> eventData = Map.of(
                    "eventType", eventType,
                    "userId", userDto.getId().toString(),
                    "name", userDto.getName(),
                    "age", userDto.getAge(),
                    "phone", userDto.getPhone()
            );
            log.info("Sending user event to Kafka: {}", eventData);
            kafkaTemplate.send(USER_EVENTS_TOPIC, userDto.getId().toString(), eventData);
        } catch (Exception e) {
            log.error("Failed to send user event for ID: {}", userDto.getId(), e);
        }
    }


}