ALTER TABLE tasks
    ADD user_id BIGINT
    REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE