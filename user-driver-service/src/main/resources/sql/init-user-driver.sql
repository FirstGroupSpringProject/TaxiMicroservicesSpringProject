CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users
(
    id    UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name  CHARACTER VARYING(200),
    age   INTEGER,
    phone CHARACTER VARYING(10)
);

CREATE TABLE drivers
(
    id               UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name             CHARACTER VARYING(200),
    orders_completed INTEGER,
    current_status   anyenum
);