CREATE TABLE IF NOT EXISTS users
(
    id              SERIAL PRIMARY KEY,
    login           VARCHAR(50) NOT NULL,
    email           VARCHAR(255),
    password        TEXT        NOT NULL,
    last_login_time TIMESTAMP
);

CREATE TABLE IF NOT EXISTS roles
(
    id        SERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role_relation
(
    user_id       INTEGER,
    role_id       INTEGER,
    assigned_time TIMESTAMP,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_data
(
    user_id      INTEGER PRIMARY KEY,
    name         VARCHAR(100),
    surname      VARCHAR(100),
    patronymic   VARCHAR(100),
    birth_date   TIMESTAMP,
    phone_number VARCHAR(18),
    passport     VARCHAR(30),
    snils        VARCHAR(100),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL
);