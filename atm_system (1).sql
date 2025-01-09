-- Create the database
CREATE DATABASE atm_system;

-- Use the created database
USE atm_system;

-- Create the 'users' table
CREATE TABLE users (
    user_id VARCHAR(50) PRIMARY KEY,
    pin VARCHAR(10) NOT NULL,
    balance DOUBLE DEFAULT 0
);

-- Create the 'transactions' table
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(50),
    type VARCHAR(20),
    amount DOUBLE,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Insert sample data into the 'users' table
INSERT INTO users (user_id, pin, balance) VALUES 
('user1', '1234', 1000),
('user2', '5678', 2000),
('user3', '0000', 500);

-- Sample transactions for testing
INSERT INTO transactions (user_id, type, amount, timestamp) VALUES
('user1', 'Deposit', 500, NOW()),
('user2', 'Withdraw', 300, NOW()),
('user3', 'Transfer', 200, NOW());
