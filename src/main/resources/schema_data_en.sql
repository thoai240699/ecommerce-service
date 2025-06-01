CREATE DATABASE IF NOT EXISTS ecommerce_service CHARACTER SET utf8mb4 COLLATE utf8mb4_vi_0900_ai_ci;
USE ecommerce_service;
-- Set charset for Vietnamese (optional in English schema, kept for compatibility)
SET NAMES 'utf8mb4';

-- Create tables
CREATE TABLE user (
    user_id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(255),
    phone_number VARCHAR(20),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE role (
    role_name VARCHAR(50) PRIMARY KEY,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE permission (
    permission_name VARCHAR(50) PRIMARY KEY,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE user_role (
    user_id VARCHAR(36) NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES user(user_id),
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_name) REFERENCES role(role_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE role_permission (
    role_name VARCHAR(50) NOT NULL,
    permission_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (role_name, permission_name),
    CONSTRAINT fk_role_permission_role FOREIGN KEY (role_name) REFERENCES role(role_name),
    CONSTRAINT fk_role_permission_permission FOREIGN KEY (permission_name) REFERENCES permission(permission_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE invalidated_token (
    token_id VARCHAR(255) PRIMARY KEY,
    expiration_time DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE address (
    address_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    street_address VARCHAR(50) NOT NULL,
    ward VARCHAR(255) NOT NULL,
    district VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES user(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE category (
    category_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;


CREATE TABLE product (
    product_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL,
    category_id VARCHAR(36) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE product_image (
    image_id VARCHAR(36) PRIMARY KEY,
    product_id VARCHAR(36) NOT NULL,
    CONSTRAINT fk_product_image_product FOREIGN KEY (product_id) REFERENCES product(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE `order` (
    order_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    address_id VARCHAR(36) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('Pending', 'Processing', 'Shipped', 'Delivered', 'Cancelled') DEFAULT 'Pending',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES user(user_id),
    CONSTRAINT fk_order_address FOREIGN KEY (address_id) REFERENCES address(address_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE order_detail (
    order_detail_id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_detail_order FOREIGN KEY (order_id) REFERENCES `order`(order_id),
    CONSTRAINT fk_order_detail_product FOREIGN KEY (product_id) REFERENCES product(product_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE payment (
    payment_id VARCHAR(36) PRIMARY KEY,
    order_id VARCHAR(36) NOT NULL,
    method VARCHAR(50) NOT NULL,
    status ENUM('Pending', 'Completed', 'Failed') DEFAULT 'Pending',
    payment_date DATETIME,
    CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES `order`(order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE review (
    review_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    product_id VARCHAR(36) NOT NULL,
    rating INT NOT NULL,
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES user(user_id),
    CONSTRAINT fk_review_product FOREIGN KEY (product_id) REFERENCES product(product_id),
    CONSTRAINT check_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci; 