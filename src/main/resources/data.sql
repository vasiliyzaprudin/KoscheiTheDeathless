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
INSERT INTO mines (mine_code, mine_name, mine_address, mine_description, mine_enabled)VALUES
                                                                  ('eKtsE2ABLpY1', 'АО «Урупский ГОК»', 'Карачаево-Черкесская Республика', 'Урупский ГОК — горно-обогатительный комбинат в Урупском районе Карачаево-Черкесии...', true),
                                                                  ('MINE-01', 'Гайский ГОК', 'Оренбургская область', 'Гайский ГОК — горно-обогатительный комбинат в городе Гай...', true),
                                                                  ('MINE-02', 'Учалинский ГОК ', 'Республика Башкортостан', 'Учалинский ГОК — горно-обогатительный комбинат в городе Учалы...', true),
                                                                  ('MINE-03', 'ООО «Башкирская медь»', 'Республика Башкортостан', 'ООО «Башкирская медь» — горнодобывающее предприятие в Хайбуллинском районе...', true),
                                                                  ('MINE-04', 'Бурибаевский ГОК', 'Республика Башкортостан', 'Бурибаевский ГОК — небольшой горно-обогатительный комбинат в селе Бурибай...', true),
                                                                  ('MINE-05', 'АО «Сибирь-полиметаллы»', 'Алтайский край, г. Змеиногорск', 'АО «Сибирь-полиметаллы» — горнодобывающее предприятие в Алтайском крае...', true),
                                                                  ('MINE-06', 'АО «Сафьяновская медь»', 'Свердловская область, г. Реж.', 'Медно-цинковый горно-обогатительный комбинат.', true)
ON CONFLICT (mine_code) DO NOTHING;

-- ==========================================
-- 3. ПОЛЬЗОВАТЕЛИ (Пароли зашифрованы)
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


