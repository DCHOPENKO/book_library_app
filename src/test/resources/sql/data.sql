DELETE FROM book;
DELETE FROM author;

INSERT INTO author (id, firstname, lastname)
VALUES (1, 'Paulo', 'Coelho'),
       (2, 'Ernest', 'Hemingway'),
       (3, 'Jack', 'London');
SELECT SETVAL('author_id_seq', (SELECT MAX(id) FROM author));

INSERT INTO book (id, title, author_id)
VALUES (1, 'The Alchemist', (SELECT id FROM author WHERE firstname = 'Paulo' AND lastname = 'Coelho')),
       (2, 'The winner stands alone', (SELECT id FROM author WHERE firstname = 'Paulo' AND lastname = 'Coelho')),
       (3, 'Valkiries', (SELECT id FROM author WHERE firstname = 'Paulo' AND lastname = 'Coelho')),
       (4, 'The Old Man and The Sea, Book Cover May Vary', (SELECT id FROM author WHERE firstname = 'Ernest' AND lastname = 'Hemingway')),
       (5, 'The Garden of Eden', (SELECT id FROM author WHERE firstname = 'Ernest' AND lastname = 'Hemingway')),
       (6, 'The Snows of Kilimanjaro and Other Stories', (SELECT id FROM author WHERE firstname = 'Ernest' AND lastname = 'Hemingway')),
       (7, 'Hemingway on Fishing', (SELECT id FROM author WHERE firstname = 'Ernest' AND lastname = 'Hemingway')),
       (8, 'The Son of the Wolf', (SELECT id FROM author WHERE firstname = 'Jack' AND lastname = 'London')),
       (9, 'The Cruise of the Dazzler', (SELECT id FROM author WHERE firstname = 'Jack' AND lastname = 'London'));
SELECT SETVAL('book_id_seq', (SELECT MAX(id) FROM book));