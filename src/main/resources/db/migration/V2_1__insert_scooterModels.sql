INSERT INTO t_scooter_model (c_id, c_max_speed, c_range_km, c_title)
VALUES (1,25, 30, 'Xiaomi Mi Electric Scooter 1S'),
       (2,30, 45, 'Xiaomi Mi Electric Scooter Pro 2'),
       (3,25, 20, 'Xiaomi Mi Electric Scooter Essential'),
       (4,35, 65, 'Xiaomi Mi Electric Scooter 3'),
       (5,40, 100, 'Xiaomi Mi Electric Scooter Ultra');

ALTER SEQUENCE t_scooter_model_c_id_seq RESTART WITH 6;

