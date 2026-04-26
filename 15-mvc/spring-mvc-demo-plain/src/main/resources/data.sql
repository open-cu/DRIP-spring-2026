-- Data for Authors (10 records)

INSERT INTO authors (name) VALUES ('Author 01');
INSERT INTO authors (name) VALUES ('Author 02');
INSERT INTO authors (name) VALUES ('Author 03');
INSERT INTO authors (name) VALUES ('Author 04');
INSERT INTO authors (name) VALUES ('Author 05');
INSERT INTO authors (name) VALUES ('Author 06');
INSERT INTO authors (name) VALUES ('Author 07');
INSERT INTO authors (name) VALUES ('Author 08');
INSERT INTO authors (name) VALUES ('Author 09');
INSERT INTO authors (name) VALUES ('Author 10');

-- Data for Journals (10 records)

INSERT INTO journals (name) VALUES ('Journal 01');
INSERT INTO journals (name) VALUES ('Journal 02');
INSERT INTO journals (name) VALUES ('Journal 03');
INSERT INTO journals (name) VALUES ('Journal 04');
INSERT INTO journals (name) VALUES ('Journal 05');
INSERT INTO journals (name) VALUES ('Journal 06');
INSERT INTO journals (name) VALUES ('Journal 07');
INSERT INTO journals (name) VALUES ('Journal 08');
INSERT INTO journals (name) VALUES ('Journal 09');
INSERT INTO journals (name) VALUES ('Journal 10');

-- Data for Articles (10 records)

INSERT INTO articles (title, journal_id) VALUES ('Article 01', 1);
INSERT INTO articles (title, journal_id) VALUES ('Article 02', 2);
INSERT INTO articles (title, journal_id) VALUES ('Article 03', 3);
INSERT INTO articles (title, journal_id) VALUES ('Article 04', 4);
INSERT INTO articles (title, journal_id) VALUES ('Article 05', 5);
INSERT INTO articles (title, journal_id) VALUES ('Article 06', 6);
INSERT INTO articles (title, journal_id) VALUES ('Article 07', 7);
INSERT INTO articles (title, journal_id) VALUES ('Article 08', 8);
INSERT INTO articles (title, journal_id) VALUES ('Article 09', 9);
INSERT INTO articles (title, journal_id) VALUES ('Article 10', 10);

-- Data for Article Authors (many-to-many mapping)

INSERT INTO article_authors (article_id, author_id) VALUES (1, 1);
INSERT INTO article_authors (article_id, author_id) VALUES (1, 2);
INSERT INTO article_authors (article_id, author_id) VALUES (1, 3);
INSERT INTO article_authors (article_id, author_id) VALUES (2, 4);
INSERT INTO article_authors (article_id, author_id) VALUES (2, 5);
INSERT INTO article_authors (article_id, author_id) VALUES (2, 6);
INSERT INTO article_authors (article_id, author_id) VALUES (3, 7);
INSERT INTO article_authors (article_id, author_id) VALUES (3, 8);
INSERT INTO article_authors (article_id, author_id) VALUES (3, 9);
INSERT INTO article_authors (article_id, author_id) VALUES (4, 10);
INSERT INTO article_authors (article_id, author_id) VALUES (4, 1);
INSERT INTO article_authors (article_id, author_id) VALUES (4, 2);
INSERT INTO article_authors (article_id, author_id) VALUES (5, 3);
INSERT INTO article_authors (article_id, author_id) VALUES (5, 4);
INSERT INTO article_authors (article_id, author_id) VALUES (5, 5);
INSERT INTO article_authors (article_id, author_id) VALUES (6, 6);
INSERT INTO article_authors (article_id, author_id) VALUES (6, 7);
INSERT INTO article_authors (article_id, author_id) VALUES (6, 8);
INSERT INTO article_authors (article_id, author_id) VALUES (7, 9);
INSERT INTO article_authors (article_id, author_id) VALUES (7, 10);
INSERT INTO article_authors (article_id, author_id) VALUES (7, 1);
INSERT INTO article_authors (article_id, author_id) VALUES (8, 2);
INSERT INTO article_authors (article_id, author_id) VALUES (8, 3);
INSERT INTO article_authors (article_id, author_id) VALUES (8, 4);
INSERT INTO article_authors (article_id, author_id) VALUES (9, 5);
INSERT INTO article_authors (article_id, author_id) VALUES (9, 6);
INSERT INTO article_authors (article_id, author_id) VALUES (9, 7);
INSERT INTO article_authors (article_id, author_id) VALUES (10, 8);
INSERT INTO article_authors (article_id, author_id) VALUES (10, 9);
INSERT INTO article_authors (article_id, author_id) VALUES (10, 10);
