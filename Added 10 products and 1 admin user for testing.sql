-- Adding 10 products and 1 admin user for testing

INSERT INTO Users (username, password, role, email, city)
VALUES ('admin', 'admin123', 'ADMIN', 'admin@shop.com', 'Nashik');

INSERT INTO Products (name, description, price, quantity) VALUES
('Laptop', '14-inch business laptop', 55000.00, 10),
('Smartphone', 'Android phone with 128GB storage', 20000.00, 15),
('Headphones', 'Wireless Bluetooth headphones', 3000.00, 25),
('Keyboard', 'Mechanical keyboard', 2500.00, 20),
('Mouse', 'Wireless mouse', 1200.00, 30),
('Monitor', '24-inch LED monitor', 8000.00, 12),
('Printer', 'All-in-one printer', 7000.00, 8),
('Tablet', '10-inch Android tablet', 15000.00, 10),
('Camera', 'DSLR camera', 45000.00, 5),
('Speaker', 'Portable Bluetooth speaker', 2200.00, 18);

select * from users

select * from products