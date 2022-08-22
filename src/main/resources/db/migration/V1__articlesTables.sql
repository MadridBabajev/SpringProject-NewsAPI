-- CREATE SEQUENCE articles_sequence
--     INCREMENT BY 1 START WITH 1;

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    release_date DATE NOT NULL,
    content TEXT NOT NULL,
    UNIQUE (title)
);