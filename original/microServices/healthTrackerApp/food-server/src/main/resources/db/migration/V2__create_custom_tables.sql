CREATE TABLE custom_foods
(
    id              BINARY(16)   NOT NULL PRIMARY KEY,
    name            VARCHAR(500) NOT NULL,
    food_class      VARCHAR(255) NOT NULL,
    measurement     VARCHAR(255) NOT NULL,
    size            DOUBLE       NOT NULL,
    user_id         BINARY(16)   NOT NULL,
    calorie_amount  DOUBLE       NOT NULL,
    calorie_unit    VARCHAR(255) NOT NULL,
    food_info       VARCHAR(255),
    food_large_info TEXT,
    food_picture    VARCHAR(5000)
);

CREATE TABLE custom_food_nutrients
(
    custom_food_id BINARY(16) NOT NULL,
    nutrient_id    BINARY(16) NOT NULL,
    CONSTRAINT fk_custom_food_nutrients_food FOREIGN KEY (custom_food_id) REFERENCES custom_foods (id),
    CONSTRAINT fk_custom_food_nutrients_nutrient FOREIGN KEY (nutrient_id) REFERENCES nutrients (id),
    PRIMARY KEY (custom_food_id, nutrient_id)
);

CREATE TABLE serving_food_portions
(
    custom_food_id BINARY(16) NOT NULL,
    portion_id     BINARY(16) NOT NULL,
    CONSTRAINT fk_serving_food_portions_food FOREIGN KEY (custom_food_id) REFERENCES custom_foods (id),
    CONSTRAINT fk_serving_food_portions_portion FOREIGN KEY (portion_id) REFERENCES serving_portions (id),
    PRIMARY KEY (custom_food_id, portion_id)
);