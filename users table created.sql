-- Users table
CREATE TABLE Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN','USER','GUEST') NOT NULL,
    email VARCHAR(100),
    city VARCHAR(50)
);

select * from users