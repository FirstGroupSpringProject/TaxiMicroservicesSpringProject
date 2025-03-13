package com.userdriverservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class UserDto {

    private UUID id;

    private String name;

    private Integer age;

    private String phone;

}
