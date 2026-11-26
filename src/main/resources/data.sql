-- ==========================================
-- 1. РОЛИ
-- ==========================================
INSERT INTO roles (code, title) VALUES
                                    ('ADMIN', 'Администратор'),
                                    ('TUNNELER', 'Проходчик'),
                                    ('SURVEYOR', 'Маркшейдер'),
                                    ('GEOLOGIST', 'Геолог'),
                                    ('MINE_MANAGER', 'Главный инженер'),
                                    ('ENGINEER', 'Пневмостулонадуватель'),
                                    ('VIEWER', 'Наблюдатель'),
                                    ('HOLDING_ANALYST', 'Главный геомеханик компании')
ON CONFLICT (code) DO NOTHING;

-- ==========================================
-- 2. РУДНИКИ (snake_case!)
-- ==========================================
INSERT INTO mines (mine_code, mine_name, mine_address, mine_description, mine_enabled) VALUES
                                                                                           ('eKtsE2ABLpY1', 'АО «Урупский ГОК»', 'Карачаево-Черкесская Республика', 'Урупский ГОК — горно-обогатительный комбинат в Урупском районе Карачаево-Черкесии...', true),
                                                                                           ('MINE-01', 'Гайский ГОК', 'Оренбургская область', 'Гайский ГОК — горно-обогатительный комбинат в городе Гай...', true),
                                                                                           ('MINE-02', 'Учалинский ГОК ', 'Республика Башкортостан', 'Учалинский ГОК — горно-обогатительный комбинат в городе Учалы...', true),
                                                                                           ('MINE-03', 'ООО «Башкирская медь»', 'Республика Башкортостан', 'ООО «Башкирская медь» — горнодобывающее предприятие в Хайбуллинском районе...', true),
                                                                                           ('MINE-04', 'Бурибаевский ГОК', 'Республика Башкортостан', 'Бурибаевский ГОК — небольшой горно-обогатительный комбинат в селе Бурибай...', true),
                                                                                           ('MINE-05', 'АО «Сибирь-полиметаллы»', 'Алтайский край, г. Змеиногорск', 'АО «Сибирь-полиметаллы» — горнодобывающее предприятие в Алтайском крае...', true),
                                                                                           ('MINE-06', 'АО «Сафьяновская медь»', 'Свердловская область, г. Реж.', 'Медно-цинковый горно-обогатительный комбинат.', true)
ON CONFLICT (mine_code) DO NOTHING;

-- ==========================================
-- 3. ПОЛЬЗОВАТЕЛИ
-- ==========================================
INSERT INTO users (familiya, name, otchestvo, username, email, password, enabled, mine_id) VALUES
                                                                                               ('Игроманов', 'Игроман', 'Игроманович', 'player', 'Igromanovich@mail.ru', '$2a$10$aI52maVnskBMKdHqomzATey/UR//17.ySI4vWE4FF5T1vbodowHMC', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Кузьмичев', 'Кузьмич', 'Кузьмичевич', 'Kuzmichev', 'Kuzmichev@mail.ru', '$2a$10$iw2qnLmWFvEH/VdLT067iu6g6YKGAltoCGroG4w8pvxAQZW26wE5O', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Пуджов', 'Пудж', 'Пуджович', 'Pudge', 'Pudg@mail.ua', '$2a$10$dphzWJcfhPDwxb5tR03UdOFZ6kSLKcW71RXGkHj3rbBtrIIt5NIJu', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Борисов', 'Борис', 'Борисович', 'user', 'andru@gmail.com', '$2a$10$TltmbSmbY0eqAkR9rwonZeiJMt69zzUSWvdDJdxayBb80qDZTYEwG', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Петров', 'kl', 'Петрович', 'scam', 'scam1@gmail.com', '$2a$10$Iv1MbrAWs01iuhEBlRfjpOXa51pwIULhr6JQWAv6L0OfwK7OzuVXa', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Борисов', 'cumshot', 'Борисович', 'user1', 'Borov12@gmail.com', '$2a$10$lCI7SjADG4PpfhFg0LXAr.M5xV48EoW1aufwfDf4xjSVRb.orfGQ.', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Тарасов', 'Тарас', 'Тарасович', 'taras', 'taras09@mail.cum', '$2a$10$2RVicKY7rTSOQN/VEXQgueIRmIh2VBVFjjIKrlmVG1B7URNmsDXy2', true, NULL),
                                                                                               ('Пупкин', 'Пупик', 'Пупикович', 'pupkin', 'pupkin@mail.ru', '$2a$10$iYMIlTF52gdxCrHSGuvrh.uxmtseT/nmk7gDuJek7bSrPlSrIupEO', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-01')),
                                                                                               ('Калачев', 'Калач', 'Калачевич', 'Kalachov', 'Kalachov@mail.ua', '$2a$10$n6M/XAytYUB7Pw9L8wqsQuHYtHD8/1ahn11380WTwKUUCexcVaHLu', true, (SELECT mine_id FROM mines WHERE mine_code = 'MINE-06'))
ON CONFLICT (username) DO NOTHING;

-- ==========================================
-- 4. ВЫДАЧА РОЛЕЙ
-- ==========================================
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'admin' AND r.code = 'ADMIN'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'player' AND r.code = 'TUNNELER'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'Kuzmichev' AND r.code = 'SURVEYOR'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'Pudge' AND r.code = 'MINE_MANAGER'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'user' AND r.code = 'GEOLOGIST'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'scam' AND r.code = 'ENGINEER'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'user1' AND r.code = 'GEOLOGIST'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'taras' AND r.code = 'HOLDING_ANALYST'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'pupkin' AND r.code = 'TUNNELER'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'Kalachov' AND r.code = 'SURVEYOR'
  AND NOT EXISTS (SELECT 1 FROM user_roles ur WHERE ur.user_id = u.id AND ur.role_id = r.id);

-- ==========================================
-- 5. ГЕОЛОГИЧЕСКИЕ КЛАССЫ
-- ==========================================
INSERT INTO geological_stability_classes (geological_stability_class_code) VALUES
                                                                               ('1b'), ('2b'), ('3b'), ('4b'), ('5b'),
                                                                               ('1p'), ('2p'), ('3p'), ('4p'), ('5p'),
                                                                               ('1s'), ('2s'), ('3s'), ('4s'), ('5s')
ON CONFLICT (geological_stability_class_code) DO NOTHING;

-- ==========================================
-- 6. СВОЙСТВА
-- ==========================================
INSERT INTO property_stabilities (property_stability_name, property_stability_name_ru, unit, data_type) VALUES
                                                                                                            ('strength', 'Прочность', 'МПа', 'число'),
                                                                                                            ('block_shape', 'Форма блоков', '-', 'текст'),
                                                                                                            ('block_size', 'Размер блоков', 'м', 'число'),
                                                                                                            ('block_cohesion', 'Сцепленность блоков', '-', 'текст'),
                                                                                                            ('block_interlocking', 'Зажатость блоков', '-', 'текст'),
                                                                                                            ('brittleness', 'Хрупкость', '-', 'текст'),
                                                                                                            ('plasticity', 'Пластичность', '-', 'текст'),
                                                                                                            ('viscosity', 'Вязкость', '-', 'текст'),
                                                                                                            ('fragment_shape', 'Форма кусков', '-', 'текст'),
                                                                                                            ('fragment_size', 'Размер кусков', 'м', 'число'),
                                                                                                            ('fragment_connectivity', 'Связность кусков', '-', 'текст'),
                                                                                                            ('slaking_under_impact', 'Осыпаемость при ударе', '-', 'текст'),
                                                                                                            ('angle_of_repose', 'Угол откоса насыпи', 'градусы', 'число'),
                                                                                                            ('grain_shape', 'Форма зерен', '-', 'текст'),
                                                                                                            ('caking_tendency', 'Слеживаемость', '-', 'текст'),
                                                                                                            ('water_content', 'Обводненность', '-', 'текст'),
                                                                                                            ('strike_azimuth_of_bedding', 'Азимут падения слоистости', 'градусы', 'число'),
                                                                                                            ('dip_angle_of_bedding', 'Угол падения слоистости', 'градусы', 'число'),
                                                                                                            ('weathering_degree', 'Выветрелость', '-', 'текст'),
                                                                                                            ('stickiness', 'Липкость', '-', 'текст'),
                                                                                                            ('volume_increase_upon_wetting', 'Увеличение объема при намокании', '-', 'текст'),
                                                                                                            ('creep', 'Ползучесть', '-', '-')
ON CONFLICT (property_stability_name) DO NOTHING;

-- ==========================================
-- 7. ДИАПАЗОНЫ ЗНАЧЕНИЙ
-- ==========================================
INSERT INTO range_value_property_stability_name (range_value_property_stability_name, range_value_property_stability_name_ru, min_value_property_stability, max_value_property_stability, property_stability_id)
SELECT v.name, v.name_ru, v.min_val, v.max_val, p.property_stability_id
FROM (VALUES
          ('very_strong', 'Весьма прочные', 150.0, 300.0, 'strength'),
          ('strong', 'Прочные', 50.0, 149.999, 'strength'),
          ('weak', 'Непрочные', 5.0, 49.999, 'strength'),
          ('very_weak', 'Весьма непрочные', 0.0, 4.999, 'strength'),
          ('regular', 'Правильная', null, null, 'block_shape'),
          ('acute_angled', 'Остроугольная', null, null, 'block_shape'),
          ('rounded', 'Округлая', null, null, 'block_shape'),
          ('large_blocks', 'Крупные', 1.0, 10.0, 'block_size'),
          ('medium_blocks', 'Средние', 0.1, 0.999, 'block_size'),
          ('small_blocks', 'Мелкие', 0.0, 0.0999, 'block_size'),
          ('well_cohesive', 'Хорошо сцепленные', null, null, 'block_cohesion'),
          ('moderately_cohesive', 'Умеренно сцепленные', null, null, 'block_cohesion'),
          ('poorly_cohesive', 'Плохо сцепленные', null, null, 'block_cohesion'),
          ('confined', 'Зажатые', null, null, 'block_interlocking'),
          ('unconfined', 'Не зажатые', null, null, 'block_interlocking'),
          ('splitting', 'Раскалываются', null, null, 'brittleness'),
          ('non_splitting', 'Не раскалываются', null, null, 'brittleness'),
          ('hard', 'Твердый', null, null, 'plasticity'),
          ('soft', 'Мягкий', null, null, 'plasticity'),
          ('viscous', 'Вязкий', null, null, 'viscosity'),
          ('non_viscous', 'Невязкий', null, null, 'viscosity'),
          ('elongated', 'Вытянутая', null, null, 'fragment_shape'),
          ('platy', 'Пластинчатая', null, null, 'fragment_shape'),
          ('crumpled', 'Мятая', null, null, 'fragment_shape'),
          ('scaly', 'Чешуйчатая', null, null, 'fragment_shape'),
          ('large_fragments', 'Крупные', 1.0, 10.0, 'fragment_size'),
          ('medium_fragments', 'Средние', 0.1, 0.999, 'fragment_size'),
          ('small_fragments', 'Мелкие', 0.0, 0.0999, 'fragment_size'),
          ('well_separated', 'Хорошо разделяются', null, null, 'fragment_connectivity'),
          ('poorly_separated', 'Плохо разделяются', null, null, 'fragment_connectivity'),
          ('heavily_crumbling', 'Сильно осыпаются', null, null, 'slaking_under_impact'),
          ('moderately_crumbling', 'Умеренно осыпаются', null, null, 'slaking_under_impact'),
          ('slightly_crumbling', 'Слабо осыпаются', null, null, 'slaking_under_impact'),
          ('steep', 'Крутой', 45.0, 90.0, 'angle_of_repose'),
          ('moderate', 'Обычный', 25.0, 44.999, 'angle_of_repose'),
          ('gentle', 'Пологий', 0.0, 24.999, 'angle_of_repose'),
          ('needle_shaped', 'Игольчатая', null, null, 'grain_shape'),
          ('angular_grain', 'Угловатая', null, null, 'grain_shape'),
          ('rounded_grain', 'Окатанная', null, null, 'grain_shape'),
          ('caking', 'Слеживаются', null, null, 'caking_tendency'),
          ('non_caking', 'Не слеживаются', null, null, 'caking_tendency'),
          ('water_saturated', 'Водонасыщенный', null, null, 'water_content'),
          ('wet', 'Мокрый', null, null, 'water_content'),
          ('moist', 'Влажный', null, null, 'water_content'),
          ('dry', 'Сухой', null, null, 'water_content'),
          ('fresh', 'Свежая', null, null, 'weathering_degree'),
          ('slightly_weathered', 'Слабо выветрелая', null, null, 'weathering_degree'),
          ('moderately_weathered', 'Средне выветрелая', null, null, 'weathering_degree'),
          ('highly_weathered', 'Сильно выветрелая', null, null, 'weathering_degree'),
          ('completely_weathered', 'Полностью выветрелая', null, null, 'weathering_degree'),
          ('sticky', 'Липкий', null, null, 'stickiness'),
          ('non_sticky', 'Нелипкий', null, null, 'stickiness'),
          ('slaking', 'Размокает', null, null, 'stickiness'),
          ('volume_increasing', 'Увеличивается в объеме', null, null, 'volume_increase_upon_wetting'),
          ('volume_stable', 'Не изменяется в объеме', null, null, 'volume_increase_upon_wetting')
     ) AS v(name, name_ru, min_val, max_val, prop_name)
         JOIN property_stabilities p ON p.property_stability_name = v.prop_name
ON CONFLICT (property_stability_id, range_value_property_stability_name) DO NOTHING;

-- ==========================================
-- 8. СВЯЗЬ КЛАССОВ И СВОЙСТВ (class_property_stability)
-- ==========================================
INSERT INTO class_property_stability (geological_stability_class_id, property_stability_id, is_required) VALUES
-- Класс 1 (1b): strength (1), brittleness (6)
(1, 1, true),
(1, 6, true),

-- Класс 2 (2b): strength (1), block_shape (2), block_size (3), block_cohesion (4), block_interlocking (5)
(2, 1, true),
(2, 2, true),
(2, 3, false),
(2, 4, false),
(2, 5, false),

-- Класс 3 (3b): strength (1), block_shape (2), block_size (3), block_cohesion (4), block_interlocking (5)
(3, 1, true),
(3, 2, true),
(3, 3, false),
(3, 4, false),
(3, 5, false),

-- Класс 4 (4b): strength (1), block_shape (2), block_size (3), block_cohesion (4), block_interlocking (5), slaking_under_impact (12)
(4, 1, true),
(4, 2, true),
(4, 3, false),
(4, 4, false),
(4, 5, false),
(4, 12, false),

-- Класс 5 (5b): strength (1), block_shape (2), block_size (3), block_cohesion (4), slaking_under_impact (12), water_content (16), brittleness (6), viscosity (8)
(5, 1, true),
(5, 2, true),
(5, 3, false),
(5, 4, false),
(5, 12, false),
(5, 16, false),
(5, 7, false),
(5, 8, false),

-- Класс 6 (1p): viscosity (8), brittleness (6), fragment_size (10), fragment_shape (9), fragment_connectivity (11), volume_increase_upon_wetting (21)
(6, 8, true),
(6, 6, true),
(6, 10, false),
(6, 9, false),
(6, 11, false),
(6, 21, false),

-- Класс 7 (2p): viscosity (8), water_content (16), fragment_size (10), fragment_shape (9), fragment_connectivity (11), volume_increase_upon_wetting (21), brittleness (6), block_interlocking (5)
(7, 8, true),
(7, 16, true),
(7, 10, false),
(7, 9, false),
(7, 11, false),
(7, 21, false),
(7, 6, false),
(7, 5, false),

-- Класс 8 (3p): viscosity (8), water_content (16), fragment_connectivity (11), volume_increase_upon_wetting (21), slaking_under_impact (12), brittleness (6)
(8, 8, true),
(8, 16, true),
(8, 11, false),
(8, 21, false),
(8, 12, false),
(8, 6, false),

-- Класс 9 (4p): viscosity (8), strike_azimuth_of_bedding (17), dip_angle_of_bedding (18), fragment_size (10), fragment_shape (9), fragment_connectivity (11), volume_increase_upon_wetting (21), block_interlocking (5), water_content (16)
(9, 8, true),
(9, 17, true),
(9, 18, false),
(9, 10, false),
(9, 9, false),
(9, 11, false),
(9, 21, false),
(9, 5, false),
(9, 16, false),

-- Класс 10 (5p): brittleness (6), water_content (16), fragment_size (10), fragment_shape (9), slaking_under_impact (12), fragment_connectivity (11), volume_increase_upon_wetting (21), stickiness (20), viscosity (8)
(10, 6, true),
(10, 16, true),
(10, 10, false),
(10, 9, false),
(10, 12, false),
(10, 11, false),
(10, 21, false),
(10, 20, false),
(10, 8, false),

-- Класс 11 (1s): slaking_under_impact (12), angle_of_repose (13), grain_shape (14), caking_tendency (15), water_content (16)
(11, 12, true),
(11, 13, true),
(11, 14, false),
(11, 15, false),
(11, 16, false),

-- Класс 12 (2s): slaking_under_impact (12), angle_of_repose (13), grain_shape (14), caking_tendency (15), water_content (16), brittleness (6), viscosity (8), fragment_connectivity (11), stickiness (20), volume_increase_upon_wetting (21)
(12, 12, true),
(12, 13, true),
(12, 14, false),
(12, 15, false),
(12, 16, false),
(12, 7, false),
(12, 8, false),
(12, 11, false),
(12, 20, false),
(12, 21, false),

-- Класс 13 (3s): slaking_under_impact (12), angle_of_repose (13), grain_shape (14), caking_tendency (15), water_content (16), block_shape (2), block_size (3), block_cohesion (4), block_interlocking (5)
(13, 12, true),
(13, 13, true),
(13, 14, false),
(13, 15, false),
(13, 16, false),
(13, 2, false),
(13, 3, false),
(13, 4, false),
(13, 5, false),

-- Класс 14 (4s): viscosity (8), slaking_under_impact (12), grain_shape (14), water_content (16), stickiness (20), volume_increase_upon_wetting (21), weathering_degree (19)
(14, 8, true),
(14, 12, true),
(14, 14, false),
(14, 16, false),
(14, 20, false),
(14, 21, false),
(14, 19, false),

-- Класс 15 (5s): viscosity (8), slaking_under_impact (12), grain_shape (14), block_shape (2), block_size (3), block_cohesion (4), caking_tendency (15), water_content (16)
(15, 8, true),
(15, 12, true),
(15, 14, false),
(15, 2, false),
(15, 3, false),
(15, 4, false),
(15, 15, false),
(15, 16, false)
ON CONFLICT (geological_stability_class_id, property_stability_id) DO NOTHING;