CREATE TABLE products (
    productId BIGSERIAL PRIMARY KEY, 
    productSku BIGINT NOT NULL,
    productName TEXT,                
    productAmount BIGINT DEFAULT 0,
    productData VARCHAR(120)
);
CREATE TABLE loyality_data  (
    productSku BIGINT NOT NULL PRIMARY KEY,
    loyalityData VARCHAR(120)
);

INSERT INTO products (productId, productSku, productName, productAmount, productData) VALUES 
(20001, 'hammer', 45,'Loyality_off'),
(30001, 'sink', 20,'Loyality_off'),
(40001, 'roof_shell', 256,'Loyality_on'),
(50001, 'priming', 67,'Loyality_off'),
(60001, 'clapboard', 120,'Loyality_on');

INSERT INTO loyality_data (productSku,loyalityData) VALUES 
(20001, 'Loyality_on'),
(30001, 'Loyality_on'),
(50001, 'Loyality_on'),
(60001, 'Loyality_on');

