CREATE TABLE IF NOT EXISTS final_grade
(
    id                 BIGSERIAL PRIMARY KEY,
    grade              VARCHAR(10) NOT NULL,
    attestation_period DATE,
    user_id            INTEGER,
    discipline_id      INTEGER,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE SET NULL,
    FOREIGN KEY (discipline_id) REFERENCES discipline (id) ON DELETE SET NULL
);



CREATE TABLE IF NOT EXISTS documents
(
    id              BIGSERIAL PRIMARY KEY,
    filename        TEXT  NOT NULL,
    mime_type       VARCHAR(100),
    upload_time     TIMESTAMP DEFAULT NOW(),
    file_data       BYTEA NOT NULL,
    file_size_bytes BIGINT
);



CREATE TABLE IF NOT EXISTS document_template
(
    id            SERIAL PRIMARY KEY,
    template_type VARCHAR(50) UNIQUE,
    template_text TEXT NOT NULL,
    mappings      TEXT NOT NULL
);