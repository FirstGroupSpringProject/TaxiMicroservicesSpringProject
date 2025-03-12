package com.orderrideservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.orderrideservice.dto.RouteDto;
import com.orderrideservice.entity.Route;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface RouteMapper {

    // Маппинг Route -> RouteDto
    RouteDto toDto(Route route);

    // Маппинг RouteDto -> Route (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    Route toEntity(RouteDto routeDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateRoute(RouteDto routeDto, @MappingTarget Route route);

}