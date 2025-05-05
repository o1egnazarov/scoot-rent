CREATE TABLE t_location_node
(
    c_id            BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_address       VARCHAR(100),
    c_location_type VARCHAR(30) NOT NULL CHECK (c_location_type IN ('COUNTRY', 'CITY', 'DISTRICT', 'RENTAL_POINT')),
    c_latitude      DECIMAL(10, 8),
    c_longitude     DECIMAL(11, 8),
    c_title         VARCHAR(50)  NOT NULL,
    c_parent_id     BIGINT,

    CONSTRAINT fk_location_node_parent FOREIGN KEY (c_parent_id) REFERENCES t_location_node (c_id)
);

CREATE INDEX idx_location_node_parent_id ON t_location_node (c_parent_id);
CREATE INDEX idx_location_node_geolocation ON t_location_node (c_latitude, c_longitude);