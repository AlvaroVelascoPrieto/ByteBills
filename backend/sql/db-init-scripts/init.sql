CREATE DATABASE IF NOT EXISTS bytebills;
USE bytebills;

CREATE TABLE IF NOT EXISTS users (
    username PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);