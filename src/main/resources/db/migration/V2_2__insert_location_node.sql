INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Россия', 'COUNTRY', NULL, NULL, 'Россия', NULL);

INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Омск', 'CITY', 54.988480, 73.324236, 'Омск, Россия', 1),
       ('Новосибирск', 'CITY', 55.030199, 82.920430, 'Новосибирск, Россия', 1),
       ('Томск', 'CITY', 56.484645, 84.948205, 'Томск, Россия', 1);


-- Омск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Центральный район', 'DISTRICT', 54.990000, 73.360000, 'Центральный район, Омск', 2),
       ('Советский район', 'DISTRICT', 54.950000, 73.400000, 'Советский район, Омск', 2);

-- Новосибирск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Центральный район', 'DISTRICT', 55.033333, 82.916667, 'Центральный район, Новосибирск', 3),
       ('Октябрьский район', 'DISTRICT', 55.020000, 82.950000, 'Октябрьский район, Новосибирск', 3);

-- Томск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Ленинский район', 'DISTRICT', 56.470000, 84.980000, 'Ленинский район, Томск', 4),
       ('Советский район', 'DISTRICT', 56.510000, 84.950000, 'Советский район, Томск', 4);


-- Омск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('ул. Ленина, 23', 'RENTAL_POINT', 54.984856, 73.367790, 'ул. Ленина, 23, Омск', 5),
       ('ул. Тарская, 12', 'RENTAL_POINT', 54.987000, 73.372000, 'ул. Тарская, 12, Омск', 5),
       ('ул. Завертяева, 5', 'RENTAL_POINT', 54.953056, 73.363889, 'ул. Завертяева, 5, Омск', 6),
       ('ул. Масленникова, 136', 'RENTAL_POINT', 54.959722, 73.358056, 'ул. Масленникова, 136, Омск', 6);

-- Новосибирск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('Красный проспект, 17', 'RENTAL_POINT', 55.041500, 82.934600, 'Красный проспект, 17, Новосибирск', 7),
       ('ул. Кирова, 86', 'RENTAL_POINT', 55.018667, 82.958972, 'ул. Кирова, 86, Новосибирск', 7),
       ('ТЦ Галерея', 'RENTAL_POINT', 55.028300, 82.920000, 'ул. Мичурина, 12, Новосибирск', 8),
       ('Речной вокзал', 'RENTAL_POINT', 55.034000, 82.896300, 'ул. Большевистская, 125, Новосибирск', 8);

-- Томск
INSERT INTO t_location_node (c_title, c_location_type, c_latitude, c_longitude, c_address, c_parent_id)
VALUES ('пр. Ленина, 30', 'RENTAL_POINT', 56.484567, 84.948891, 'пр. Ленина, 30, Томск', 9),
       ('ул. Усова, 5', 'RENTAL_POINT', 56.472222, 84.949999, 'ул. Усова, 5, Томск', 9),
       ('ул. Белинского, 15', 'RENTAL_POINT', 56.509400, 84.956600, 'ул. Белинского, 15, Томск', 10),
       ('ул. Пушкина, 10', 'RENTAL_POINT', 56.511100, 84.953000, 'ул. Пушкина, 10, Томск', 10);