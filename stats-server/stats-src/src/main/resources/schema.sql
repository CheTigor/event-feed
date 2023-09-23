DROP TABLE IF EXISTS endpoint_hit CASCADE;

CREATE TABLE IF NOT EXISTS endpoint_hit (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app VARCHAR(50),
    uri VARCHAR(50),
    ip VARCHAR(15),
    created timestamp,
    CONSTRAINT uq_hit_id UNIQUE(id)
);