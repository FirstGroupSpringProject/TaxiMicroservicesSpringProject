package com.orderrideservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.orderrideservice.dto.OrderDto;
import com.orderrideservice.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDto toDto(Order order);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    Order toEntity(OrderDto orderDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "paymentId", ignore = true)
    @Mapping(target = "userId", ignore = true)
    void updateOrder(OrderDto orderDto, @MappingTarget Order order);
}