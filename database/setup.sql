-- Create Database
CREATE DATABASE IF NOT EXISTS VeriFiDB;
USE VeriFiDB;

-- Create Users Table
CREATE TABLE IF NOT EXISTS users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    national_id VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    dob DATE NOT NULL,
    encrypt_pass_num VARCHAR(255) NOT NULL,
    issue_date DATE NOT NULL,
    expiry_date DATE NOT NULL,
    country VARCHAR(50) NOT NULL
);

-- Create Temporary Table for CSV Import
CREATE TABLE IF NOT EXISTS users_temp (
    id INT PRIMARY KEY AUTO_INCREMENT,
    national_id VARCHAR(20),
    full_name VARCHAR(100),
    dob_str VARCHAR(10),         -- Store DOB as a string before conversion
    encrypt_pass_num VARCHAR(255),
    issue_date_str VARCHAR(10),  -- Store Issue Date as a string before conversion
    expiry_date_str VARCHAR(10), -- Store Expiry Date as a string before conversion
    country VARCHAR(50)
);

-- Load Data from CSV into Temporary Table
LOAD DATA LOCAL INFILE 'D:/Study/git/identity-system/VeriFidb.csv'
INTO TABLE users_temp
FIELDS TERMINATED BY ',' 
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;

-- Convert String Dates and Move Data to Main Users Table
INSERT INTO users (national_id, full_name, dob, encrypt_pass_num, issue_date, expiry_date, country)
SELECT 
    national_id,
    full_name,
    STR_TO_DATE(dob_str, '%d-%m-%Y'),
    encrypt_pass_num,
    STR_TO_DATE(issue_date_str, '%d-%m-%Y'),
    STR_TO_DATE(expiry_date_str, '%d-%m-%Y'),
    country
FROM users_temp;

-- Drop Temporary Table After Data Migration
DROP TABLE users_temp;
