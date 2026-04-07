INSERT INTO author (name, email) VALUES ('Alice Johnson', 'alice@example.com');
INSERT INTO author (name, email) VALUES ('Bob Smith',     'bob@example.com');

INSERT INTO article (title, text, status)
VALUES ('Introduction to Spring Transactions', 'ACID properties and isolation levels.', 'PUBLISHED');

INSERT INTO article (title, text, status)
VALUES ('Spring JDBC Deep Dive', 'JdbcTemplate, RowMapper, ResultSetExtractor.', 'DRAFT');

INSERT INTO article_author VALUES (1, 1);
INSERT INTO article_author VALUES (2, 1), (2, 2);
