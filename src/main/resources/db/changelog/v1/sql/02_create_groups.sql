CREATE TABLE IF NOT EXISTS student_group
(
    id            SERIAL PRIMARY KEY,
    group_name    VARCHAR(50) UNIQUE NOT NULL,
    curator_id    INTEGER,
    head_id       INTEGER UNIQUE,
    course        INTEGER,
    entrance_year INTEGER,
    FOREIGN KEY (curator_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (head_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_group_relation
(
    user_id  INTEGER UNIQUE,
    group_id INTEGER,
    added_at DATE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (group_id) REFERENCES student_group (id) ON DELETE CASCADE
);