DROP TABLE IF EXISTS articles;

CREATE TABLE articles (
    id BIGSERIAL PRIMARY KEY,
    title TEXT NOT NULL,
    author TEXT NOT NULL,
    release_date DATE NOT NULL,
    content TEXT NOT NULL,
    UNIQUE (title)
);