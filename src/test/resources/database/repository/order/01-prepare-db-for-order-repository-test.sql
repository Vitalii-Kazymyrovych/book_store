INSERT INTO users (email, password, first_name, last_name, shipping_address)
VALUES ('randomUser123@domain.com', 'password', 'first name', 'last name', 'address');

INSERT INTO orders (user_id, status, order_date, total, shipping_address) VALUES
(1, 'NEW', '2024-03-29 15:44:10', 100.00, 'address'),
(1, 'NEW', '2024-03-29 15:44:10', 150.00, 'address');

INSERT INTO categories (name, description) VALUES ('category', 'description.');

INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Book Title 1', 'Author Name 1', '978-0-00-000000-1', 19.99, 'Description for Book 1', 'https://coverimage1.jpg'),
('Book Title 2', 'Author Name 2', '978-0-00-000000-2', 29.99, 'Description for Book 2', 'https://coverimage2.jpg');

INSERT INTO book_category (book_id, category_id) VALUES
(1, 1),
(2, 1);

INSERT INTO order_items (order_id, book_id, quantity, price, is_deleted) VALUES
(1, 1, 2, 19.99, false),
(1, 2, 1, 29.99, false);
