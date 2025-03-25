package com.userdriverservice.mapper;

import com.userdriverservice.dto.DriverDto;
import com.userdriverservice.entity.Driver;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverDto toDto(Driver driver);

    @Mapping(target = "id", ignore = true)
    Driver toEntity(DriverDto driverDto);

    @Mapping(target = "id", ignore = true)
    void updateDriver(DriverDto driverDto, @MappingTarget Driver driver);
}