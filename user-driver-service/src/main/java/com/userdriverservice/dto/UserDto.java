package com.userdriverservice.dto;

import lombok.Data;

import java.util.UUID;
/**
 * DTO для представления данных пользователя.
 * Используется для передачи информации о пользователе между слоями приложения.
 */
@Data
public class UserDto {

    private UUID id;

    private String name;

    private Integer age;

    private String phone;

}
