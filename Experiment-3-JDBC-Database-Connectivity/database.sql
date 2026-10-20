-- Run this in MySQL before running JdbcDemo.java

CREATE DATABASE college;

USE college;

CREATE TABLE student(
    id INT PRIMARY KEY,
    name VARCHAR(30),
    branch VARCHAR(20)
);

-- Optional sample rows (JdbcDemo inserts 101 itself)
INSERT INTO student VALUES (102, 'Amit', 'IT');
INSERT INTO student VALUES (103, 'Neha', 'ECE');

SHOW TABLES;
DESC student;
