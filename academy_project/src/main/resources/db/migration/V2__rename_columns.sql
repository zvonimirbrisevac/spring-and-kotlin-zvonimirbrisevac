ALTER TABLE cars RENAME COLUMN addeddate TO added_date;
ALTER TABLE cars RENAME COLUMN productionyear TO production_year;
ALTER TABLE cars RENAME COLUMN serialnumber TO serial_number;

ALTER TABLE checkups RENAME COLUMN timeanddate TO time_and_date;
ALTER TABLE checkups RENAME COLUMN workername TO worker_name;
ALTER TABLE checkups RENAME COLUMN carid TO car_id;

ALTER TABLE checkups ADD CONSTRAINT  fk_cars_id FOREIGN KEY (car_id) REFERENCES cars (id);




