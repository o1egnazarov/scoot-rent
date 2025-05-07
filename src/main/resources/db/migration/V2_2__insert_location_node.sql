INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Россия', 'COUNTRY', NULL, NULL, 'Россия', NULL);

INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Омск', 'CITY', 54.990400, 73.366975, 'Омск, Россия', 1),
       ('Новосибирск', 'CITY', 55.030204, 82.920430, 'Новосибирск, Россия', 1),
       ('Томск', 'CITY', 56.484645, 84.947649, 'Томск, Россия', 1);


-- Омск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Центральный район', 'DISTRICT', 55.004804, 73.418599, 'Центральный район, Омск', 2),
       ('Советский район', 'DISTRICT', 55.041462, 73.281480, 'Советский район, Омск', 2);

-- Новосибирск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Центральный район', 'DISTRICT', 55.036705, 82.929179, 'Центральный район, Новосибирск', 3),
       ('Октябрьский район', 'DISTRICT', 55.010519, 83.010432, 'Октябрьский район, Новосибирск', 3);

-- Томск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Ленинский район', 'DISTRICT', 56.524908, 84.948017, 'Ленинский район, Томск', 4),
       ('Советский район', 'DISTRICT', 56.477254, 85.014097, 'Советский район, Томск', 4);


-- Омск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('ул. Ленина, 23', 'RENTAL_POINT', 54.980430, 73.378453, 'ул. Ленина, 23, Омск', 5),
       ('ул. Тарская, 12', 'RENTAL_POINT', 54.990876, 73.368841, 'ул. Тарская, 12, Омск', 5),
       ('ул. Завертяева, 5', 'RENTAL_POINT', 55.035194, 73.439745, 'ул. Завертяева, 5, Омск', 6),
       ('ул. Масленникова, 136', 'RENTAL_POINT', 54.974540, 73.416874, 'ул. Масленникова, 136, Омск', 6);

-- Новосибирск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Красный проспект, 17', 'RENTAL_POINT', 55.026597, 82.920744, 'Красный проспект, 17, Новосибирск', 7),
       ('ул. Кирова, 86', 'RENTAL_POINT', 55.013998, 82.949634, 'ул. Кирова, 86, Новосибирск', 7),
       ('ТЦ Галерея', 'RENTAL_POINT', 55.043721, 82.922343, 'ул. Гоголя, 13, Новосибирск', 8),
       ('Речной вокзал', 'RENTAL_POINT', 55.008373, 82.938241, 'ул. Большевистская, 125, Новосибирск', 8);

-- Томск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('пр. Ленина, 30', 'RENTAL_POINT', 56.465390, 84.950164, 'пр. Ленина, 30, Томск', 9),
       ('ул. Усова, 7', 'RENTAL_POINT', 56.463749, 84.954709, 'ул. Усова, 7, Томск', 9),
       ('ул. Белинского, 15', 'RENTAL_POINT', 56.472620, 84.957386, 'ул. Белинского, 15, Томск', 10),
       ('ул. Пушкина, 10', 'RENTAL_POINT', 56.495479, 84.956497, 'ул. Пушкина, 10, Томск', 10);