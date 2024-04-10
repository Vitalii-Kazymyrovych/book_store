DELETE FROM cart_items;
ALTER TABLE cart_items AUTO_INCREMENT = 1;

DELETE FROM shopping_carts;
ALTER TABLE shopping_carts AUTO_INCREMENT = 1;

DELETE FROM book_category;
ALTER TABLE book_category AUTO_INCREMENT = 1;

DELETE FROM order_items;
ALTER TABLE order_items AUTO_INCREMENT = 1;

DELETE FROM books;
ALTER TABLE books AUTO_INCREMENT = 1;

DELETE FROM categories;
ALTER TABLE categories AUTO_INCREMENT = 1;

DELETE FROM orders;
ALTER TABLE orders AUTO_INCREMENT = 1;

DELETE FROM user_role;
ALTER TABLE user_role AUTO_INCREMENT = 1;

DELETE FROM users;
ALTER TABLE users AUTO_INCREMENT = 1;
