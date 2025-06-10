ALTER TABLE users
    ADD CONSTRAINT login_unique UNIQUE (login);


ALTER TABLE user_group_relation
    ALTER COLUMN added_at
        SET DEFAULT now();


ALTER TABLE discipline
    ADD CONSTRAINT discipline_name_unique UNIQUE (name);


ALTER TABLE task
DROP
COLUMN task_contractor;


ALTER TABLE task
    ADD group_id INTEGER;


ALTER TABLE task
    ADD FOREIGN KEY (group_id) REFERENCES student_group (id);


ALTER TABLE task
    RENAME COLUMN task_author TO task_author_id;


ALTER TABLE task_user
    RENAME TO task_user_relation;


ALTER TABLE task_document
    RENAME TO task_document_relation;