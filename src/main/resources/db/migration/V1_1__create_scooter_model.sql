CREATE TABLE t_scooter_model
(
    c_id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_title     VARCHAR(50) NOT NULL,
    c_max_speed INTEGER     NOT NULL,
    c_range_km  INTEGER     NOT NULL
);
