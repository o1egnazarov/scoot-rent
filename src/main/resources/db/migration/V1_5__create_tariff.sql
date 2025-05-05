CREATE TABLE t_tariff
(
    c_id                BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    c_duration_unit     VARCHAR(30) CHECK (c_duration_unit IN ('HOUR', 'DAY', 'WEEK', 'MONTH', 'YEAR')),
    c_duration_value    INTEGER,
    c_is_active         BOOLEAN,
    c_price_per_minute  NUMERIC(38, 2),
    c_sub_duration_days INTEGER,
    c_title             VARCHAR(50)  NOT NULL,
    c_type              VARCHAR(30) NOT NULL CHECK (c_type IN ('DEFAULT_TARIFF', 'SPECIAL_TARIFF', 'SUBSCRIPTION')),
    c_unlock_fee        INTEGER,
    c_valid_from        TIMESTAMP(6),
    c_valid_until       TIMESTAMP(6)
);