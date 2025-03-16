package com.aston.paymentbillingservice.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Интрефейс Service для управления объектами.
 * Предоставляет операции для работы с объектами такими как получение,
 * создание, обновление и удаление.
 *
 * @param <T> объект типа T, с которой будет работать Service
 */
@Service
public interface ServiceInterface<T> {

    /**
     * Получает список всех объектов.
     *
     * @return список всех объектов типа T
     */
    List<T> findAll();

    /**
     * Получает объект по заданному идентификатору.
     *
     * @param id - идентификатор
     * @return объект Optional типа T, если найден
     */
    Optional<T> findById(UUID id);

    /**
     * Сохраняет новый объект.
     *
     * @param t - объект типа T, который необходимо сохранить
     * @return сохранённый объект Optional типа T
     */
    T save(T t);

    /**
     * Удаляет объект по заданному идентификатору.
     *
     * @param id - идентификатор объекта типа T
     * @return объект Optional типа T, если удаление прошло успешно
     */
    Optional<T> delete(UUID id);

    /**
     * Обновляет существующий объект.
     *
     * @param id - идентификатор
     * @param t  - новые данные для обновлёния объекта
     * @return объект Optional типа T, если обновление прошло успешно
     */
    Optional<T> update(UUID id, T t);
}