ALTER TABLE cars DROP CONSTRAINT fk_models_id;
ALTER TABLE cars ADD CONSTRAINT fk_models_id FOREIGN KEY (model_id) REFERENCES models(id)
    ON DELETE CASCADE;