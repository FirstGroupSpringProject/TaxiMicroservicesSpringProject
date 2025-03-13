package com.orderrideservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.Ride;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface RideMapper {

    // Маппинг Ride -> RideDto
    RideDto toDto(Ride ride);

    // Маппинг RideDto -> Ride (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    Ride toEntity(RideDto rideDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateRide(RideDto rideDto, @MappingTarget Ride ride);

}
