CREATE TABLE t_rental_point
(
    c_id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_address   VARCHAR(100),
    c_latitude  DECIMAL(10, 8),
    c_longitude DECIMAL(11, 8),
    c_title     VARCHAR(50) NOT NULL,
    c_parent_id BIGINT,

    CONSTRAINT fk_rental_point_parent FOREIGN KEY (c_parent_id) REFERENCES t_rental_point (c_id)
);

CREATE INDEX idx_rental_point_parent_id ON t_rental_point (c_parent_id);
CREATE INDEX idx_rental_point_geolocation ON t_rental_point (c_latitude, c_longitude);