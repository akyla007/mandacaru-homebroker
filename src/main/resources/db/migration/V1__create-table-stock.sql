
CREATE DATABASE IF NOT EXISTS mandacaru_broker;
USE mandacaru_broker;

CREATE TABLE stock (
                       id VARCHAR(255) PRIMARY KEY,
                       symbol VARCHAR(255) NOT NULL,
                       company_name VARCHAR(255) NOT NULL,
                       price FLOAT NOT NULL
);

INSERT INTO stock (id, symbol, company_name, price) VALUES
    ('1', 'AAPL5', 'Apple Inc.', 150.00);

INSERT INTO stock (id, symbol, company_name, price) VALUES
    ('2', 'GOOG3', 'Alphabet Inc.', 2500.00);

INSERT INTO stock (id, symbol, company_name, price) VALUES
    ('3', 'MSFT2', 'Microsoft Corporation', 300.50);
