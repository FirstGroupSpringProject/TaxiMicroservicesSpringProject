CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Удаляем таблицу, если она уже существует (для тестов)
DROP TABLE IF EXISTS users;

-- Создаём таблицу пользователей
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       name VARCHAR(200) NOT NULL,
                       phone VARCHAR(15) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(50) NOT NULL
);

-- Вставляем тестовых пользователей
INSERT INTO users (name, phone, password, role) VALUES
                                                    ('admin', '1234567890', '$2a$10$E/hgUfbGiR0oGxW8ROnLqeN1M3lPHJDk/AjIjShQLWHEcNNwAwh3q', 'ADMIN'),
                                                    ('john_doe', '9876543210', '$2a$10$TrS7G8dT0vhxPXcBlrF.5Oh6bJcjbU8E2p8WXuDymRplhMftvZcme', 'USER');
