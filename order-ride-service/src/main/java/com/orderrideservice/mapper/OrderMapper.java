package com.orderrideservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.orderrideservice.dto.OrderDto;
import com.orderrideservice.entity.Order;

@Mapper(componentModel = "spring") // Интеграция с Spring
public interface OrderMapper {

    // Маппинг Order -> OrderDto
    OrderDto toDto(Order order);

    // Маппинг OrderDto -> Order (игнорируем id при создании)
    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderDto orderDto);

    // Обновление существующей сущности из Dto
    @Mapping(target = "id", ignore = true)
    void updateOrder(OrderDto orderDto, @MappingTarget Order order);

}
