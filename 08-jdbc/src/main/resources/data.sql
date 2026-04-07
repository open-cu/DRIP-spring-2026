-- Справочник площадок публикации
INSERT INTO venue (name) VALUES ('Nature');
INSERT INTO venue (name) VALUES ('Science');
INSERT INTO venue (name) VALUES ('IEEE Transactions');
INSERT INTO venue (name) VALUES ('ACM Computing Surveys');

-- Авторы
INSERT INTO author (name) VALUES ('Alice Johnson');
INSERT INTO author (name) VALUES ('Bob Smith');
INSERT INTO author (name) VALUES ('Carol White');

-- Статьи
INSERT INTO article (title, text, venue_id)
VALUES ('Introduction to Spring JDBC',
        'Spring JDBC simplifies database access by reducing boilerplate code.',
        (SELECT id FROM venue WHERE name = 'IEEE Transactions'));

INSERT INTO article (title, text, venue_id)
VALUES ('Design Patterns in Java',
        'A comprehensive overview of GoF patterns applied to modern Java.',
        (SELECT id FROM venue WHERE name = 'ACM Computing Surveys'));

-- Авторы статей
INSERT INTO article_author (article_id, author_id)
VALUES (1, 1), (1, 2);

INSERT INTO article_author (article_id, author_id)
VALUES (2, 2), (2, 3);

-- Рецензии
INSERT INTO article_review (text, article_id)
VALUES ('Solid introduction, good examples.', 1);

INSERT INTO article_review (text, article_id)
VALUES ('Missing coverage of behavioral patterns.', 2);
