INSERT INTO users (name, email) VALUES ('Иван', 'ivan@mail.ru') ON CONFLICT DO NOTHING;
INSERT INTO users (name, email) VALUES ('Мария', 'maria@mail.ru') ON CONFLICT DO NOTHING;