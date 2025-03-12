package com.userdriverservice.mapper;

import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface DriverMapper {

    // Маппинг Driver -> DriverDto
    DriverDto toDto(Driver driver);

    // Маппинг DriverDto -> Driver (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    Driver toEntity(DriverDto driverDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateDriver(DriverDto driverDto, @MappingTarget Driver driver);
}
