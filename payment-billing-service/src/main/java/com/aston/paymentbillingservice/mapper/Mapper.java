package com.aston.paymentbillingservice.mapper;

import org.springframework.stereotype.Component;

/**
 * Интерфейс Mapper отвечает за преобразование между объектами типа DTO и Entity.
 * Данный интерфайс предоставляет методы для преобразования данных из
 * объектов передачи данных (DTO) в сущности (Entity) и обратно.
 *
 * @param <E> - объект типа Entity
 * @param <T> - объект типа DTO
 */
@Component
public interface Mapper<E, T> {
    /**
     * Преобразует объект типа Entity в объект типа DTO.
     *
     * @param e - объект Entity, который необходимо преобразовать
     * @return преобразованный объект DTO
     */
    T toDto(E e);

    /**
     * Преобразует объект типа DTO в объект типа Entity.
     *
     * @param t - объект DTO, который необходимо преобразовать
     * @return преобразованный объект Entity
     */
    E toEntity(T t);
}
