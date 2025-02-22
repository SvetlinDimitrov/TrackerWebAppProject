CREATE TABLE nutrients
(
    id     BINARY(16)   NOT NULL PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    unit   VARCHAR(255) NOT NULL,
    amount DOUBLE       NOT NULL
);

CREATE TABLE serving_portions
(
    id            BINARY(16)   NOT NULL PRIMARY KEY,
    metric        VARCHAR(255) NOT NULL,
    serving_weight DOUBLE       NOT NULL,
    amount        DOUBLE       NOT NULL,
    is_main       BOOLEAN      NOT NULL
);
