CREATE TABLE records
(
    id      BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    daily_calories DOUBLE NOT NULL,
    date    DATETIME   NOT NULL UNIQUE
);