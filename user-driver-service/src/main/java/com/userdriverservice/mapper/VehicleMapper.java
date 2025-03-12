package com.userdriverservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.userdriverservice.dto.VehicleDto;
import com.userdriverservice.entity.Vehicle;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface VehicleMapper {

    // Маппинг User -> UserDto
    VehicleDto toDto(Vehicle vehicle);

    // Маппинг UserDto -> User (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    Vehicle toEntity(VehicleDto vehicleDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateVehicle(VehicleDto vehicleDto, @MappingTarget Vehicle vehicle);
}
