INSERT INTO categories (name, description) VALUES ('category', 'description.');

INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Book Title 1', 'Author Name 1', '978-0-00-000000-1', 19.99, 'Description for Book 1', 'https://coverimage1.jpg'),
('Book Title 2', 'Author Name 2', '978-0-00-000000-2', 29.99, 'Description for Book 2', 'https://coverimage2.jpg');

INSERT INTO book_category (book_id, category_id) VALUES
(1, 1),
(2, 1);