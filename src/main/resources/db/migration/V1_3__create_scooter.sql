CREATE TABLE t_scooter
(
    c_id               BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_duration_in_used NUMERIC(21),
    c_number_plate     VARCHAR(10) NOT NULL UNIQUE,
    c_status           VARCHAR(30) NOT NULL CHECK (c_status IN ('TAKEN', 'FREE', 'NOT_ACTIVE')),
    c_model_id         BIGINT      NOT NULL,
    c_rental_point_id  BIGINT,

--     TODO не знаю как тут лучше сделать по сути логично удалять самокат при удалении модели
    CONSTRAINT fk_scooter_model FOREIGN KEY (c_model_id) REFERENCES t_scooter_model (c_id) ON DELETE CASCADE,
    CONSTRAINT fk_rental_point FOREIGN KEY (c_rental_point_id) REFERENCES t_location_node (c_id) ON DELETE SET NULL
);

CREATE INDEX idx_scooter_model_id ON t_scooter (c_model_id);
CREATE INDEX idx_scooter_rental_point_id ON t_scooter (c_rental_point_id);