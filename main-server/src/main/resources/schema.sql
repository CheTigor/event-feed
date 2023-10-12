drop table IF EXISTS users CASCADE;
drop table IF EXISTS categories CASCADE;
drop table IF EXISTS events CASCADE;
drop table IF EXISTS requests CASCADE;
drop table IF EXISTS compilations CASCADE;
drop table IF EXISTS compilation_events CASCADE;
drop table IF EXISTS comments CASCADE;
drop table IF EXISTS responses_comment CASCADE;

create TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR,
    name VARCHAR,
    CONSTRAINT uq_user_email UNIQUE(email)
);

create TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR,
    CONSTRAINT uq_categories_name UNIQUE(name)
);

create TABLE IF NOT EXISTS events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation VARCHAR,
    category_id BIGINT,
    confirmed_requests INT,
    created_on TIMESTAMP,
    description VARCHAR,
    event_date TIMESTAMP,
    initiator_id BIGINT,
    location_lat FLOAT,
    location_lon FLOAT,
    paid BOOLEAN,
    participant_limit INT,
    published_on TIMESTAMP,
    request_moderation BOOLEAN,
    state VARCHAR,
    title VARCHAR,
    views BIGINT,
    CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
    CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id)
);

create TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    created TIMESTAMP,
    event_id BIGINT,
    requester_id BIGINT,
    status VARCHAR,
    CONSTRAINT fk_req_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
    CONSTRAINT fk_req_to_events FOREIGN KEY(event_id) REFERENCES events(id)
);

create TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN,
    title VARCHAR
);

create TABLE IF NOT EXISTS compilation_events (
    compilation_id BIGINT,
    event_id BIGINT,
    CONSTRAINT fk_ce_to_comp FOREIGN KEY(compilation_id) REFERENCES compilations(id) ON delete CASCADE,
    CONSTRAINT fk_ce_to_events FOREIGN KEY(event_id) REFERENCES events(id) ON delete CASCADE,
    UNIQUE(compilation_id, event_id)
);

create TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR,
    event_id BIGINT,
    user_id BIGINT,
    created TIMESTAMP,
    its_response BOOLEAN,
    CONSTRAINT fk_com_to_events FOREIGN KEY(event_id) REFERENCES events(id) ON delete CASCADE,
    CONSTRAINT fk_com_to_users FOREIGN KEY(user_id) REFERENCES users(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS responses_comment (
    main_comment_id BIGINT,
    response_id BIGINT,
    CONSTRAINT fk_ac_to_comments FOREIGN KEY(main_comment_id) REFERENCES comments(id) ON delete CASCADE,
    CONSTRAINT fk_ac_to_response FOREIGN KEY(response_id) REFERENCES comments(id) ON delete CASCADE,
    UNIQUE(main_comment_id, response_id)
);