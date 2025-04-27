INSERT INTO t_tariff(c_duration_unit, c_duration_value, c_is_active, c_price_per_unit, c_sub_duration_days,
                     c_title, c_type, c_unlock_fee, c_valid_from, c_valid_until)
VALUES (null, null, true, 15, null, 'Default tariff', 'DEFAULT_TARIFF', 45, '2024-01-01 00:00:00',
        '2025-12-31 23:59:59'),
       ('DAY', 1, true, 10, null, 'Daily Basic', 'SPECIAL_TARIFF', 25, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       ('WEEK', 1, true, 8, null, 'Weekly Standard', 'SPECIAL_TARIFF', 15, '2024-01-01 00:00:00',
        '2025-12-31 23:59:59'),
       ('MONTH', 1, true, 5, null, 'Monthly Premium', 'SPECIAL_TARIFF', 10, '2024-01-01 00:00:00',
        '2025-12-31 23:59:59'),
       ('YEAR', 1, true, 3, null, 'Year Premium', 'SPECIAL_TARIFF', 5, '2024-01-01 00:00:00',
        '2025-12-31 23:59:59'),
       ('MONTH', 1, true, null, 30, 'Month subscription', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00',
        '2025-12-31 23:59:59');