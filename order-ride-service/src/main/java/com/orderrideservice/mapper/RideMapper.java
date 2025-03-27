package com.orderrideservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.orderrideservice.dto.RideDto;
import com.orderrideservice.entity.Ride;

@Mapper(componentModel = "spring")
public interface RideMapper {

    RideDto toDto(Ride ride);

    @Mapping(target = "id", ignore = true)
    Ride toEntity(RideDto rideDto);


    @Mapping(target = "id", ignore = true)
    void updateRide(RideDto rideDto, @MappingTarget Ride ride);

}
