CREATE TABLE IF NOT EXISTS task
(
    id              SERIAL PRIMARY KEY,
    task_name       VARCHAR(100) NOT NULL,
    task_type       VARCHAR(40),
    task_author     INTEGER,
    task_contractor INTEGER,
    is_completed    BOOLEAN DEFAULT false,
    has_documents   BOOLEAN DEFAULT false,
    FOREIGN KEY (task_author) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (task_contractor) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS task_user
(
    task_id INTEGER,
    user_id INTEGER,
    checked BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (task_id, user_id),
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS task_document
(
    task_id     INTEGER,
    document_id BIGINT,
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
    FOREIGN KEY (document_id) REFERENCES documents (id) ON DELETE CASCADE
);