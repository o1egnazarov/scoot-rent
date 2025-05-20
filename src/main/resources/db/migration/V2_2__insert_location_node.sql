INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (1, 'Россия', 'COUNTRY', NULL, NULL, 'Россия', NULL);

INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (2, 'Омск', 'CITY', 54.990400, 73.366975, 'Омск, Россия', 1),
       (3, 'Новосибирск', 'CITY', 55.030204, 82.920430, 'Новосибирск, Россия', 1),
       (4, 'Томск', 'CITY', 56.484645, 84.947649, 'Томск, Россия', 1);


-- Омск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (5, 'Центральный район', 'DISTRICT', 55.004804, 73.418599, 'Центральный район, Омск', 2),
       (6, 'Советский район', 'DISTRICT', 55.041462, 73.281480, 'Советский район, Омск', 2);

-- Новосибирск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (7, 'Центральный район', 'DISTRICT', 55.036705, 82.929179, 'Центральный район, Новосибирск', 3),
       (8, 'Октябрьский район', 'DISTRICT', 55.010519, 83.010432, 'Октябрьский район, Новосибирск', 3);

-- Томск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (9, 'Ленинский район', 'DISTRICT', 56.524908, 84.948017, 'Ленинский район, Томск', 4),
       (10, 'Советский район', 'DISTRICT', 56.477254, 85.014097, 'Советский район, Томск', 4);


-- Омск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (11, 'ул. Ленина, 23', 'RENTAL_POINT', 54.980430, 73.378453, 'ул. Ленина, 23, Омск', 5),
       (12, 'ул. Тарская, 12', 'RENTAL_POINT', 54.990876, 73.368841, 'ул. Тарская, 12, Омск', 5),
       (13, 'ул. Завертяева, 5', 'RENTAL_POINT', 55.035194, 73.439745, 'ул. Завертяева, 5, Омск', 6),
       (14, 'ул. Масленникова, 136', 'RENTAL_POINT', 54.974540, 73.416874, 'ул. Масленникова, 136, Омск', 6);

-- Новосибирск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (15, 'Красный проспект, 17', 'RENTAL_POINT', 55.026597, 82.920744, 'Красный проспект, 17, Новосибирск', 7),
       (16, 'ул. Кирова, 86', 'RENTAL_POINT', 55.013998, 82.949634, 'ул. Кирова, 86, Новосибирск', 7),
       (17, 'ТЦ Галерея', 'RENTAL_POINT', 55.043721, 82.922343, 'ул. Гоголя, 13, Новосибирск', 8),
       (18, 'Речной вокзал', 'RENTAL_POINT', 55.008373, 82.938241, 'ул. Большевистская, 125, Новосибирск', 8);

-- Томск
INSERT INTO t_location_node (c_id, c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES (19, 'пр. Ленина, 30', 'RENTAL_POINT', 56.465390, 84.950164, 'пр. Ленина, 30, Томск', 9),
       (20, 'ул. Усова, 7', 'RENTAL_POINT', 56.463749, 84.954709, 'ул. Усова, 7, Томск', 9),
       (21, 'ул. Белинского, 15', 'RENTAL_POINT', 56.472620, 84.957386, 'ул. Белинского, 15, Томск', 10),
       (22, 'ул. Пушкина, 10', 'RENTAL_POINT', 56.495479, 84.956497, 'ул. Пушкина, 10, Томск', 10);

ALTER SEQUENCE t_location_node_c_id_seq RESTART WITH 23;
