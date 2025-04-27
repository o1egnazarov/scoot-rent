CREATE TABLE t_scooter_model
(
    c_id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_title     VARCHAR(50) NOT NULL,
    c_max_speed INTEGER     NOT NULL,
    c_range_km  INTEGER     NOT NULL
);

CREATE TABLE t_rental_point
(
    c_id        BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_address   VARCHAR(100),
    c_latitude  DECIMAL(10, 8),
    c_longitude DECIMAL(11, 8),
    c_title     VARCHAR(50) NOT NULL,
    c_parent    BIGINT,

    CONSTRAINT fk_rental_point_parent FOREIGN KEY (c_parent) REFERENCES t_rental_point (c_id) ON DELETE CASCADE
);

CREATE TABLE t_scooter
(
    c_id               BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_duration_in_used NUMERIC(21),
    c_number_plate     VARCHAR(10)  NOT NULL,
    c_status           VARCHAR(255) NOT NULL CHECK (c_status IN ('TAKEN', 'FREE', 'NOT_ACTIVE')),
    c_model            BIGINT       NOT NULL,
    c_rental_point     BIGINT,

--     TODO не знаю как тут лучше сделать по сути логично удалять самокат при удалении модели
    CONSTRAINT fk_scooter_model FOREIGN KEY (c_model) REFERENCES t_scooter_model (c_id) ON DELETE CASCADE,
    CONSTRAINT fk_rental_point FOREIGN KEY (c_rental_point) REFERENCES t_rental_point (c_id) ON DELETE SET NULL
);

CREATE TABLE t_user
(
    c_id               BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_date_of_birthday DATE        NOT NULL,
    c_email            VARCHAR(50) NOT NULL UNIQUE,
    c_password         VARCHAR(255),
    c_phone            VARCHAR(16) NOT NULL UNIQUE,
    c_role             VARCHAR(255) CHECK (c_role IN ('ROLE_ADMIN', 'ROLE_USER', 'ROLE_MODERATOR')),
    c_username         VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE t_tariff
(
    c_id                BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_duration_unit     VARCHAR(255) CHECK (c_duration_unit IN ('HOUR', 'DAY', 'WEEK', 'MONTH', 'YEAR')),
    c_duration_value    INTEGER,
    c_is_active         BOOLEAN,
    c_price_per_unit    NUMERIC(38, 2),
    c_sub_duration_days INTEGER,
    c_title             VARCHAR(50)  NOT NULL,
    c_type              VARCHAR(255) NOT NULL CHECK (c_type IN ('DEFAULT_TARIFF', 'SPECIAL_TARIFF', 'SUBSCRIPTION')),
    c_unlock_fee        INTEGER,
    c_valid_from        TIMESTAMP(6),
    c_valid_until       TIMESTAMP(6)
);

CREATE TABLE t_user_tariffs
(
    c_id           BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_discount_pct INTEGER,
    c_valid_from   TIMESTAMP(6),
    c_valid_until  TIMESTAMP(6),
    c_tariff_id    BIGINT NOT NULL,
    c_user_id      BIGINT NOT NULL,

    CONSTRAINT fk_user_tariff_tariff FOREIGN KEY (c_tariff_id) REFERENCES t_tariff (c_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_tariff_user FOREIGN KEY (c_user_id) REFERENCES t_user (c_id) ON DELETE CASCADE
);

CREATE TABLE t_user_subscriptions
(
    c_id                 BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_minute_usage_limit INTEGER,
    c_minutes_used       INTEGER,
    c_start_date         TIMESTAMP(6),
    c_end_date           TIMESTAMP(6),
    c_tariff_id          BIGINT NOT NULL,
    c_user_id            BIGINT NOT NULL,

    CONSTRAINT fk_user_subscription_tariff FOREIGN KEY (c_tariff_id) REFERENCES t_tariff (c_id) ON DELETE CASCADE,
    CONSTRAINT fk_user_subscription_user FOREIGN KEY (c_user_id) REFERENCES t_user (c_id) ON DELETE CASCADE
);

CREATE TABLE t_rental
(
    c_id                BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_cost              NUMERIC(38, 2),
    c_duration_in_pause NUMERIC(21),
    c_start_time        TIMESTAMP(6),
    c_end_time          TIMESTAMP(6),
    c_last_pause_time   TIMESTAMP(6),
    c_rental_status     VARCHAR(255) NOT NULL CHECK (c_rental_status IN ('ACTIVE', 'COMPLETED', 'PAUSE')),
    c_scooter_id        BIGINT       NOT NULL,
    c_start_point       BIGINT,
    c_end_point         BIGINT,
    c_subscription_id   BIGINT,
    c_tariff_id         BIGINT       NOT NULL,
    c_user_id           BIGINT       NOT NULL,

    CONSTRAINT fk_rental_scooter FOREIGN KEY (c_scooter_id) REFERENCES t_scooter (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_start_point FOREIGN KEY (c_start_point) REFERENCES t_rental_point (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_end_point FOREIGN KEY (c_end_point) REFERENCES t_rental_point (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_subscription FOREIGN KEY (c_subscription_id) REFERENCES t_user_subscriptions (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_tariff FOREIGN KEY (c_tariff_id) REFERENCES t_tariff (c_id) ON DELETE SET NULL,
    CONSTRAINT fk_rental_user FOREIGN KEY (c_user_id) REFERENCES t_user (c_id) ON DELETE SET NULL
);
