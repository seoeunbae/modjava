
-- Create users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- Create products table
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

-- Insert a test user
INSERT INTO users (username, password) VALUES ('testuser@example.com', 'password123');

-- Insert a sample product
INSERT INTO products (name, price) VALUES ('Sample Product', 19.99);
