CREATE DATABASE IF NOT EXISTS bytebills;
USE bytebills;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
);

CREATE TABLE IF NOT EXISTS stocks (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS crypto (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user VARCHAR(255) NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    buy_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sold BOOLEAN NOT NULL DEFAULT FALSE,
    sell_price DECIMAL(10, 2),
    sell_timestamp TIMESTAMP,
    FOREIGN KEY (user) REFERENCES users(username),
    FOREIGN KEY (stock) REFERENCES stocks(symbol)
);

CREATE TABLE IF NOT EXISTS crypto_transaction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user VARCHAR(255) NOT NULL,
    crypto_symbol VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    buy_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sold BOOLEAN NOT NULL DEFAULT FALSE,
    sell_timestamp TIMESTAMP,
    FOREIGN KEY (user) REFERENCES users(username),
    FOREIGN KEY (crypto) REFERENCES crypto(symbol)
);