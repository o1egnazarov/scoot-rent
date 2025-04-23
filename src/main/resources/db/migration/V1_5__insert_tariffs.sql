INSERT INTO t_tariff(c_id, c_duration_unit, c_duration_value, с_is_active, c_price_per_unit, c_sub_duration_days, c_title, c_type, c_unlock_fee, c_valid_from, c_valid_untill)
VALUES (1, 'DAY', 1, true, 100, 1, 'Daily Basic', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (2, 'WEEK', 1, true, 500, 7, 'Weekly Standard', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (3, 'MONTH', 1, true, 1500, 30, 'Monthly Premium', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (4, 'YEAR', 1, true, 12000, 365, 'Year Premium', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (5, 'HOUR', 4, true, 50, null, '4-Hour Express', 'PER_HOUR', 50, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (6, 'DAY', 3, true, 250, 3, '3-Day Special', 'SUBSCRIPTION', 10, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (7, 'MONTH', 3, true, 4000, 90, 'Quarterly Business', 'SUBSCRIPTION', 200, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (8, 'HOUR', 1, true, 30, null, '30-Minute Trial', 'PER_HOUR', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (9, 'MONTH', 6, true, 2000, 180, 'Half-Year Pro', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (10, 'YEAR', 2, false, 20000, 730, 'Biennial Legacy', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
       (11, 'DAY', 7, true, 600, 7, 'Weekly Plus', 'SUBSCRIPTION', 0, '2024-01-01 00:00:00', '2025-12-31 23:59:59'),
       (12, 'MONTH', 12, true, 10000, 365, 'Annual Platinum', 'SUBSCRIPTION', 2000, '2024-01-01 00:00:00', '2025-12-31 23:59:59');