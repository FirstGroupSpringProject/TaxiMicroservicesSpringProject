package com.userdriverservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.userdriverservice.dto.UserDto;
import com.userdriverservice.entity.User;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface UserMapper {

    // Маппинг User -> UserDto
    UserDto toDto(User user);

    // Маппинг UserDto -> User (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateUser(UserDto userDto, @MappingTarget User user);
}
