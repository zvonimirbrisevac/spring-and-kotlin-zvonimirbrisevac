SELECT setval('cars_id_seq', COALESCE((SELECT MAX(id)+1 FROM cars), 1), false);
SELECT setval('checkups_id_seq', COALESCE((SELECT MAX(id)+1 FROM checkups), 1), false);

SELECT setval('car_seq', COALESCE((SELECT MAX(id)+1 FROM cars), 1), false);
SELECT setval('checkup_seq', COALESCE((SELECT MAX(id)+1 FROM checkups), 1), false);