INSERT INTO t_tariff(c_id, c_duration_unit, c_duration_value, c_is_active, c_price_per_unit, c_sub_duration_days,
                     c_title, c_type, c_billing_mode, c_unlock_fee, c_valid_from, c_valid_until)
VALUES
-- Дефолтные тарифы (по минутам/по часам)
(1, null, null, true, 3, null, 'Default tariff per minute',
 'DEFAULT_TARIFF', 'PER_MINUTE', 45, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),

(2, null, null, true, 200, null, 'Default tariff per hour',
 'DEFAULT_TARIFF', 'PER_HOUR', 40, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),


-- Специальные тарифы
(3, 'DAY', 1, true, 10, null, 'Daily Basic',
 'SPECIAL_TARIFF', 'PER_MINUTE', 25, '2024-01-01 00:00:00', '2026-12-31 23:59:59'),

(4, 'WEEK', 1, true, 8, null, 'Weekly Standard',
 'SPECIAL_TARIFF', 'PER_MINUTE', 15, '2024-01-01 00:00:00', '2026-12-31 23:59:59'),

(5, 'MONTH', 1, true, 5, null, 'Monthly Premium',
 'SPECIAL_TARIFF', 'PER_MINUTE', 10, '2024-01-01 00:00:00', '2026-12-31 23:59:59'),

(6, 'YEAR', 1, true, 3, null, 'Year Premium',
 'SPECIAL_TARIFF', 'PER_MINUTE', 5, '2024-01-01 00:00:00', '2026-12-31 23:59:59'),


-- Подписки
(7, 'MONTH', 1, true, null, 30, 'Standard month subscription',
 'SUBSCRIPTION', 'PER_MINUTE', 0, '2024-01-01 00:00:00', '2026-12-31 23:59:59'),

(8, 'MONTH', 1, true, null, 30, 'Premium month subscription',
 'SUBSCRIPTION', 'PER_MINUTE', 0, '2024-01-01', '2026-12-31'),

(9, 'YEAR', 1, true, null, 365, 'Gold year subscription',
 'SUBSCRIPTION', 'PER_MINUTE', 0, '2024-01-01', '2026-12-31');

ALTER SEQUENCE t_tariff_c_id_seq RESTART WITH 10;
