CREATE DATABASE IF NOT EXISTS bytebills;
USE bytebills;

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(255) PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stocks (
    symbol VARCHAR(10) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS stock_user (
    username VARCHAR(255) NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (stock_symbol) REFERENCES stocks(symbol),
    PRIMARY KEY (username, stock_symbol)
);

CREATE TABLE IF NOT EXISTS stock_transaction (
    username VARCHAR(255) NOT NULL,
    stock_symbol VARCHAR(10) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    buy_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sold BOOLEAN NOT NULL DEFAULT FALSE,
    sell_price DECIMAL(10, 2),
    sell_timestamp TIMESTAMP,
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (stock_symbol) REFERENCES stocks(symbol),
    PRIMARY KEY (username, stock_symbol, buy_timestamp)
);

CREATE TABLE IF NOT EXISTS dividend (
    received_on TIMESTAMP NOT NULL,
    username VARCHAR(255) NOT NULL,
    stock_symbol VARCHAR(255) NOT NULL,
    value_euros DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (stock_symbol) REFERENCES stocks(symbol),
    FOREIGN KEY (username) REFERENCES users(username),
    PRIMARY KEY (username, received_on, stock_symbol)
);

INSERT INTO users VALUES ('admin', 'admin', 'admin')