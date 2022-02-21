CREATE TABLE IF NOT EXISTS models (
    id BIGSERIAL PRIMARY KEY ,
    manufacturer VARCHAR(30),
    model_name VARCHAR(30),
    is_common BOOLEAN,
    UNIQUE (manufacturer, model_name)
);

ALTER SEQUENCE car_seq RESTART WITH 1;
ALTER SEQUENCE checkup_seq RESTART WITH 1;

CREATE SEQUENCE IF NOT EXISTS model_seq;

-- ALTER TABLE cars ADD COLUMN model VARCHAR(25)

ALTER TABLE cars DROP COLUMN manufacturer;
ALTER TABLE cars ADD COLUMN model_id BIGINT;
ALTER TABLE cars ADD CONSTRAINT fk_models_id FOREIGN KEY (model_id) REFERENCES models(id);