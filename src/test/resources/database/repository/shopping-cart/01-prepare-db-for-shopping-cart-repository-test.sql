INSERT INTO categories (name, description) VALUES ('category', 'description.');

INSERT INTO books (title, author, isbn, price, description, cover_image) VALUES
('Book Title 1', 'Author Name 1', 'ISBN-000-000-0001', 19.99, 'Description for Book 1', 'coverimage1.jpg'),
('Book Title 2', 'Author Name 2', 'ISBN-000-000-0002', 29.99, 'Description for Book 2', 'coverimage2.jpg');

INSERT INTO book_category (book_id, category_id) VALUES
(1, 1),
(2, 1);

INSERT INTO users (id, email, password, first_name, last_name, shipping_address)
VALUES (1, 'uniqueUser456@sample.net', 'password', 'first name', 'last name', 'address');

INSERT INTO shopping_carts (user_id) VALUES (1);

INSERT INTO cart_items (shopping_cart_id, book_id, quantity) VALUES (1, 1, 10);
INSERT INTO cart_items (shopping_cart_id, book_id, quantity) VALUES (1, 2, 20);
