CREATE TABLE t_rental
(
    c_id                 BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_cost               NUMERIC(38, 2),
    c_duration_in_pause  BIGINT,
    c_duration_of_rental BIGINT,
    c_start_time         TIMESTAMP(6),
    c_end_time           TIMESTAMP(6),
    c_last_pause_time    TIMESTAMP(6),
    c_rental_status      VARCHAR(30) NOT NULL CHECK (c_rental_status IN ('ACTIVE', 'COMPLETED', 'PAUSE')),
    c_scooter_id         BIGINT      NOT NULL,
    c_start_point_id     BIGINT,
    c_end_point_id       BIGINT,
    c_tariff_id          BIGINT      NOT NULL,
    c_user_id            BIGINT      NOT NULL,

    CONSTRAINT fk_rental_scooter FOREIGN KEY (c_scooter_id) REFERENCES t_scooter (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_start_point FOREIGN KEY (c_start_point_id) REFERENCES t_location_node (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_end_point FOREIGN KEY (c_end_point_id) REFERENCES t_location_node (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_tariff FOREIGN KEY (c_tariff_id) REFERENCES t_tariff (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_user FOREIGN KEY (c_user_id) REFERENCES t_user (c_id) ON DELETE SET NULL
);

CREATE INDEX idx_rental_user_id ON t_rental (c_user_id);
CREATE INDEX idx_rental_scooter_id ON t_rental (c_scooter_id);
CREATE INDEX idx_rental_tariff_id ON t_rental (c_tariff_id);
CREATE INDEX idx_rental_start_point_id ON t_rental (c_start_point_id);
CREATE INDEX idx_rental_end_point_id ON t_rental (c_end_point_id);