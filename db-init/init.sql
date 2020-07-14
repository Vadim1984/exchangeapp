CREATE DATABASE test_db;
CREATE USER 'test_user'@'%' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON *.* TO 'test_user'@'%';

USE test_db;

CREATE TABLE IF NOT EXISTS commission (
    id INT AUTO_INCREMENT PRIMARY KEY,
    commission_from VARCHAR(10),
    commission_to VARCHAR(10),
    rate DECIMAL(10,2)
);
