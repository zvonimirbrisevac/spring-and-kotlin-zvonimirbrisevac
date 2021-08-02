ALTER TABLE checkups DROP constraint fk_cars_id;
ALTER TABLE checkups ADD CONSTRAINT fk_cars_id FOREIGN KEY (car_id)
    REFERENCES cars(id) ON DELETE CASCADE;

DELETE FROM cars;
DELETE FROM checkups;