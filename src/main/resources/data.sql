
-- ==========================================
-- 1. РОЛИ (Обязательно сначала)
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
-- 2. РУДНИКИ
-- ==========================================
INSERT INTO mines (code, name, address, description, enabled) VALUES
                                                                  ('eKtsE2ABLpY1', 'АО «Урупский ГОК»', 'Карачаево-Черкесская Республика', 'Урупский ГОК — горно-обогатительный комбинат в Урупском районе Карачаево-Черкесии...', true),
                                                                  ('MINE-01', 'Гайский ГОК', 'Оренбургская область', 'Гайский ГОК — горно-обогатительный комбинат в городе Гай...', true),
                                                                  ('MINE-02', 'Учалинский ГОК ', 'Республика Башкортостан', 'Учалинский ГОК — горно-обогатительный комбинат в городе Учалы...', true),
                                                                  ('MINE-03', 'ООО «Башкирская медь»', 'Республика Башкортостан', 'ООО «Башкирская медь» — горнодобывающее предприятие в Хайбуллинском районе...', true),
                                                                  ('MINE-04', 'Бурибаевский ГОК', 'Республика Башкортостан', 'Бурибаевский ГОК — небольшой горно-обогатительный комбинат в селе Бурибай...', true),
                                                                  ('MINE-05', 'АО «Сибирь-полиметаллы»', 'Алтайский край, г. Змеиногорск', 'АО «Сибирь-полиметаллы» — горнодобывающее предприятие в Алтайском крае...', true),
                                                                  ('MINE-06', 'АО «Сафьяновская медь»', 'Свердловская область, г. Реж.', 'Медно-цинковый горно-обогатительный комбинат.', true)
    ON CONFLICT (code) DO NOTHING;

-- ==========================================
-- 3. ПОЛЬЗОВАТЕЛИ (Пароли зашифрованы)
-- ==========================================
INSERT INTO users (familiya, name, otchestvo, username, email, password, enabled, mine_id) VALUES
                                                                                               ('Игроманов', 'Игроман', 'Игроманович', 'player', 'Igromanovich@mail.ru', '$2a$10$aI52maVnskBMKdHqomzATey/UR//17.ySI4vWE4FF5T1vbodowHMC', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Кузьмичев', 'Кузьмич', 'Кузьмичевич', 'Kuzmichev', 'Kuzmichev@mail.ru', '$2a$10$iw2qnLmWFvEH/VdLT067iu6g6YKGAltoCGroG4w8pvxAQZW26wE5O', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Пуджов', 'Пудж', 'Пуджович', 'Pudge', 'Pudg@mail.ua', '$2a$10$dphzWJcfhPDwxb5tR03UdOFZ6kSLKcW71RXGkHj3rbBtrIIt5NIJu', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Борисов', 'Борис', 'Борисович', 'user', 'andru@gmail.com', '$2a$10$TltmbSmbY0eqAkR9rwonZeiJMt69zzUSWvdDJdxayBb80qDZTYEwG', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Петров', 'kl', 'Петрович', 'scam', 'scam1@gmail.com', '$2a$10$Iv1MbrAWs01iuhEBlRfjpOXa51pwIULhr6JQWAv6L0OfwK7OzuVXa', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Борисов', 'cumshot', 'Борисович', 'user1', 'Borov12@gmail.com', '$2a$10$lCI7SjADG4PpfhFg0LXAr.M5xV48EoW1aufwfDf4xjSVRb.orfGQ.', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Тарасов', 'Тарас', 'Тарасович', 'taras', 'taras09@mail.cum', '$2a$10$2RVicKY7rTSOQN/VEXQgueIRmIh2VBVFjjIKrlmVG1B7URNmsDXy2', true, NULL),
                                                                                               ('Пупкин', 'Пупик', 'Пупикович', 'pupkin', 'pupkin@mail.ru', '$2a$10$iYMIlTF52gdxCrHSGuvrh.uxmtseT/nmk7gDuJek7bSrPlSrIupEO', true, (SELECT id FROM mines WHERE code = 'MINE-01')),
                                                                                               ('Калачев', 'Калач', 'Калачевич', 'Kalachov', 'Kalachov@mail.ua', '$2a$10$n6M/XAytYUB7Pw9L8wqsQuHYtHD8/1ahn11380WTwKUUCexcVaHLu', true, (SELECT id FROM mines WHERE code = 'MINE-06'))
    ON CONFLICT (username) DO NOTHING;


-- ==========================================
-- 4. ВЫДАЧА РОЛЕЙ (Связываем по коду роли и логину)
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
-- 5. СПРАВОЧНИК ГЕОЛОГИЧЕСКИХ КЛАССОВ (15 штук)
-- ==========================================
INSERT INTO geological_stability_classes (geological_stability_class_code) VALUES
                                                                               ('1b'), ('2b'), ('3b'), ('4b'), ('5b'),
                                                                               ('1p'), ('2p'), ('3p'), ('4p'), ('5p'),
                                                                               ('1s'), ('2s'), ('3s'), ('4s'), ('5s')
ON CONFLICT (geological_stability_class_code) DO NOTHING;

-- ==========================================
-- ==========================================
-- 6. СПРАВОЧНИК СВОЙСТВ (22 штуки)
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
-- 7. СПРАВОЧНИК ДИАПАЗОНОВ ЗНАЧЕНИЙ (55 штук)
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
ON CONFLICT DO NOTHING;

-- ==========================================
