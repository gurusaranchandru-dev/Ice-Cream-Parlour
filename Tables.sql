create database IceCreamParlour;
use IceCreamParlour;

CREATE TABLE IceCream (
    id INT PRIMARY KEY,
    category VARCHAR(50),
    name VARCHAR(50),
    flavor VARCHAR(50),
    price DOUBLE,
    quantity INT
);

SELECT * FROM IceCream;

CREATE TABLE Toppings (
    toppingId INT PRIMARY KEY,
    toppingName VARCHAR(50),
    price DOUBLE,
    quantity INT
);
SELECT * FROM IceCream;


SELECT * FROM Toppings;

CREATE TABLE Orders (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    iceCreamId INT,
    quantity INT,
    totalPrice DOUBLE
);

SELECT * FROM Orders;

CREATE TABLE Bill (
    billId INT PRIMARY KEY AUTO_INCREMENT,
    orderId INT,
    grandTotal DOUBLE
);

SELECT * FROM Bill;

CREATE TABLE Sales (
    saleId INT PRIMARY KEY AUTO_INCREMENT,
    orderId INT,
    amount DOUBLE
);

SELECT * FROM Sales;

SHOW TABLES;

ALTER TABLE IceCream
ADD description VARCHAR(500),
ADD origin VARCHAR(150),
ADD raw_materials VARCHAR(500);

ALTER TABLE IceCream
MODIFY flavor VARCHAR(100);


ALTER TABLE Orders
CHANGE iceCreamId menuItemId INT;

ALTER TABLE IceCream
MODIFY name VARCHAR(100),
MODIFY flavor VARCHAR(100);

ALTER TABLE Toppings
ADD description VARCHAR(300),
ADD origin VARCHAR(150),
ADD raw_materials VARCHAR(300);

ALTER TABLE Toppings
MODIFY toppingName VARCHAR(100);

SHOW TABLES;
-- #################################################################################
-- ICE CREAM PARLOUR - DATABASE SETUP
-- Run this file in MySQL Workbench/phpMyAdmin.

CREATE DATABASE IF NOT EXISTS IceCreamParlour;
USE IceCreamParlour;

SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS bills;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS menu_categories;
DROP TABLE IF EXISTS admin;
SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE admin (
    admin_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE menu_categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE menu_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    category_id INT NOT NULL,
    item_name VARCHAR(150) NOT NULL,
    flavour VARCHAR(100),
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    description TEXT,
    origin VARCHAR(150),
    raw_materials TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    CONSTRAINT fk_menu_category FOREIGN KEY (category_id) REFERENCES menu_categories(category_id),
    CONSTRAINT chk_menu_price CHECK (price >= 0),
    CONSTRAINT chk_menu_quantity CHECK (quantity >= 0)
);

CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED'
);

CREATE TABLE order_items (
    order_item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    item_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
    CONSTRAINT fk_order_item_menu FOREIGN KEY (item_id) REFERENCES menu_items(item_id)
);

CREATE TABLE bills (
    bill_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL UNIQUE,
    bill_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    grand_total DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_bill_order FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

INSERT INTO admin(username,password,status) VALUES
('admin','admin123','ACTIVE'),
('chandru','chandru@003','ACTIVE');

INSERT INTO menu_categories(category_name,description) VALUES
('Classic Flavours','Traditional ice cream flavours'),
('Exotic Flavours','Premium and unique flavours'),
('Fruit Fusions','Fruit based ice cream flavours'),
('Signature','Special signature creations'),
('Crispy Bites','Crispy snacks and side items'),
('Beverages','Milkshakes, juices and non-alcoholic beverages');

INSERT INTO menu_items(category_id,item_name,flavour,price,quantity,description,origin,raw_materials,status)
SELECT category_id,'Vanilla','Vanilla',80,25,'Smooth and creamy ice cream with a delicate sweet vanilla aroma.','Tropical vanilla tradition','Milk, cream, sugar, vanilla, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Classic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Chocolate','Chocolate',90,25,'Rich and creamy chocolate ice cream with a deep cocoa flavour.','Mesoamerican chocolate tradition','Milk, cream, sugar, cocoa, chocolate, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Classic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Strawberry','Strawberry',90,20,'Creamy strawberry ice cream with a refreshing fruity berry taste.','European strawberry dessert tradition','Milk, cream, sugar, strawberry pulp, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Classic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Butterscotch','Butterscotch',100,20,'Creamy ice cream with rich buttery caramel and crunchy toffee notes.','Yorkshire, England','Milk, cream, sugar, butter, caramel, vanilla','AVAILABLE' FROM menu_categories WHERE category_name='Classic Flavours';

INSERT INTO menu_items SELECT NULL,category_id,'Saffron Pistachio','Saffron Pistachio',140,15,'Luxurious creamy ice cream combining floral saffron with roasted pistachio.','Indian and Persian dessert tradition','Milk, cream, sugar, saffron, pistachios, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Lotus Biscoff','Lotus Biscoff',150,15,'Creamy caramel-spiced ice cream with crunchy caramelised biscuit pieces.','Belgium','Milk, cream, sugar, Biscoff biscuits, Biscoff spread, cinnamon','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Red Velvet','Red Velvet',140,15,'Smooth red velvet inspired ice cream with mild cocoa and vanilla notes.','American dessert tradition','Milk, cream, sugar, cocoa, vanilla, red colouring','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Hazelnut','Hazelnut',150,15,'Rich nutty ice cream with roasted hazelnut aroma and creamy texture.','Piedmont, Italy inspired','Milk, cream, sugar, roasted hazelnuts, hazelnut paste','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Cotton Candy','Cotton Candy',130,15,'Fun and sweet ice cream inspired by the light flavour of cotton candy.','American confectionery tradition','Milk, cream, sugar, cotton candy flavour, food colouring','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';
INSERT INTO menu_items SELECT NULL,category_id,'Belgium Chocolate','Belgium Chocolate',160,15,'Extra-rich chocolate ice cream with an intense cocoa and chocolate character.','Belgian chocolate tradition','Milk, cream, sugar, Belgian-style chocolate, cocoa, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Exotic Flavours';

INSERT INTO menu_items SELECT NULL,category_id,'Alphonso Mango','Alphonso Mango',140,20,'Rich and aromatic mango ice cream made with the sweet flavour of Alphonso mango.','Konkan region, Maharashtra, India','Milk, cream, sugar, Alphonso mango pulp, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Fruit Fusions';
INSERT INTO menu_items SELECT NULL,category_id,'Sitaphal','Sitaphal',130,15,'Creamy fruit ice cream with the naturally sweet custard-apple flavour.','Indian custard apple tradition','Milk, cream, sugar, custard apple pulp, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Fruit Fusions';
INSERT INTO menu_items SELECT NULL,category_id,'Tender Coconut','Tender Coconut',130,15,'Light and creamy coconut ice cream with a fresh tender coconut flavour.','South and Southeast Asian tropical tradition','Milk, cream, sugar, tender coconut pulp, coconut milk','AVAILABLE' FROM menu_categories WHERE category_name='Fruit Fusions';
INSERT INTO menu_items SELECT NULL,category_id,'Guava Chilli','Guava Chilli',130,15,'Sweet guava ice cream with a mild chilli kick for a sweet and spicy taste.','Modern fusion flavour','Milk, cream, guava pulp, sugar, chilli, salt','AVAILABLE' FROM menu_categories WHERE category_name='Fruit Fusions';
INSERT INTO menu_items SELECT NULL,category_id,'Lychee','Lychee',130,15,'Delicate and refreshing ice cream with the floral sweetness of lychee.','China and Asian fruit tradition','Milk, cream, sugar, lychee pulp, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Fruit Fusions';

INSERT INTO menu_items SELECT NULL,category_id,'Brown Brunos','Brown Brunos',180,10,'Premium house-style dessert with rich chocolate and brownie-inspired flavours.','House signature creation','Milk, cream, sugar, chocolate, brownie pieces, chocolate sauce','AVAILABLE' FROM menu_categories WHERE category_name='Signature';
INSERT INTO menu_items SELECT NULL,category_id,'Black Truffle Gelato','Black Truffle',220,8,'Premium creamy gelato with an earthy and aromatic black truffle character.','Italian-inspired gelato tradition','Milk, cream, sugar, black truffle, milk solids','AVAILABLE' FROM menu_categories WHERE category_name='Signature';
INSERT INTO menu_items SELECT NULL,category_id,'Ube Makapuno','Ube Makapuno',190,10,'Purple ube ice cream combined with sweet and tender coconut strands.','Philippines','Milk, cream, ube, sugar, coconut milk, makapuno','AVAILABLE' FROM menu_categories WHERE category_name='Signature';

INSERT INTO menu_items SELECT NULL,category_id,'French Fries','Potato',90,20,'Golden crispy potato sticks with a soft and fluffy centre.','Belgian and French culinary tradition','Potatoes, vegetable oil, salt','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';
INSERT INTO menu_items SELECT NULL,category_id,'Smiles','Potato',100,20,'Crispy fun-shaped potato bites with a soft seasoned potato centre.','Modern potato snack','Potatoes, potato flakes, starch, vegetable oil, salt','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';
INSERT INTO menu_items SELECT NULL,category_id,'Fried Ice Cream','Fried Ice Cream',150,10,'Frozen ice cream covered with a crispy coating for a unique hot and cold dessert.','Multi-cultural dessert tradition','Ice cream, breadcrumbs, flour, egg or batter, frying oil','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';
INSERT INTO menu_items SELECT NULL,category_id,'Crispy Caramel Toast','Caramel',110,15,'Golden toasted bread covered with a crunchy caramelised coating.','House-style snack','Bread, butter, sugar, caramel, cinnamon, vanilla','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';
INSERT INTO menu_items SELECT NULL,category_id,'Crunchy Nutella Balls','Nutella',140,15,'Crispy bite-sized balls with a creamy hazelnut and cocoa centre.','Italian-inspired confectionery','Nutella, biscuit crumbs, chocolate, flour, cooking oil','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';
INSERT INTO menu_items SELECT NULL,category_id,'Momos - Fried / Steamed','Momos',130,15,'Filled dumplings served either crispy fried or soft steamed.','Tibetan and Himalayan culinary tradition','Flour, vegetables or paneer, onion, garlic, ginger, spices','AVAILABLE' FROM menu_categories WHERE category_name='Crispy Bites';

INSERT INTO menu_items SELECT NULL,category_id,'Oreo Milkshake','Oreo',140,15,'Thick creamy milkshake blended with Oreo cookies and ice cream.','Modern American-style milkshake','Milk, ice cream, Oreo cookies, sugar, chocolate sauce','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Brownie Milkshake','Brownie',150,15,'Rich chocolate milkshake blended with soft brownie pieces.','Modern dessert beverage','Milk, chocolate ice cream, brownie, chocolate sauce','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'KitKat Milkshake','KitKat',150,15,'Creamy chocolate milkshake blended with crunchy KitKat pieces.','Modern confectionery beverage','Milk, ice cream, KitKat, chocolate sauce','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Double Choco Milkshake','Double Chocolate',160,15,'Extra-rich chocolate milkshake with an intense double chocolate flavour.','Modern chocolate beverage','Milk, chocolate ice cream, cocoa, chocolate sauce, chocolate pieces','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'All Flavour Ice Cream Milkshake','Mixed Ice Cream',150,15,'Thick milkshake prepared using the customer selected ice cream flavour.','Modern ice cream parlour beverage','Milk, selected ice cream, sugar, flavour syrup','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Fresh Fruit Juices','Fresh Fruit',100,15,'Refreshing juice prepared using fresh seasonal fruits.','Global fruit beverage tradition','Fresh fruits, water, ice, optional sugar','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Flavoured Lemonades','Lemonade',100,15,'Refreshing sweet and tangy lemonade with your choice of flavour.','Global lemonade tradition','Lemon juice, water, sugar, fruit flavour, ice','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Flavoured Mojitos','Mojito',120,15,'Refreshing mint and lime cooler prepared as a non-alcoholic mocktail.','Cuban-inspired beverage','Lime, mint, sugar syrup, soda, ice, fruit flavour','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Coke','Cola',60,30,'Chilled carbonated cola with a sweet and refreshing taste.','Atlanta, USA','Carbonated water, sweetener, caramel colour, acids, cola flavour','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';
INSERT INTO menu_items SELECT NULL,category_id,'Pepsi','Cola',60,30,'Chilled carbonated cola with a sweet and refreshing cola flavour.','New Bern, North Carolina, USA','Carbonated water, sweetener, caramel colour, acids, cola flavour','AVAILABLE' FROM menu_categories WHERE category_name='Beverages';

SELECT 'Database setup completed.' AS message;
SELECT category_id,category_name FROM menu_categories ORDER BY category_id;
SELECT item_id,category_id,item_name,price,quantity FROM menu_items ORDER BY category_id,item_id;