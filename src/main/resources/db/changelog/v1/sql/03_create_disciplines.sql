CREATE TABLE IF NOT EXISTS discipline
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_discipline_relation
(
    user_id       INTEGER,
    discipline_id INTEGER,
    assigned_date DATE,
    PRIMARY KEY (user_id, discipline_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (discipline_id) REFERENCES discipline (id) ON DELETE CASCADE
);