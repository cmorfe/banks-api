CREATE TABLE IF NOT EXISTS banks
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL UNIQUE,
    type       VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS branches
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    code       VARCHAR(4)   NOT NULL UNIQUE,
    address    VARCHAR(200) NOT NULL,
    phone      VARCHAR(15)  NOT NULL,
    bank_id    BIGINT       NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (bank_id) REFERENCES banks (id) ON DELETE CASCADE
);
