-- hệ thống ecommerce
-- Chạy theo thứ tự: schema -> roles -> permissions -> role_permissions
CREATE DATABASE IF NOT EXISTS ecommerce_service CHARACTER SET utf8mb4 COLLATE utf8mb4_vi_0900_ai_ci;
USE ecommerce_service;
-- Thiết lập charset cho tiếng Việt
SET NAMES 'utf8mb4';

-- 1. Tạo bảng
CREATE TABLE nguoi_dung (
    ma_nguoi_dung VARCHAR(36) PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE,
    ho_ten VARCHAR(255),
    so_dien_thoai VARCHAR(20),
    thoi_gian_tao DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE vai_tro (
    ten_vai_tro VARCHAR(50) PRIMARY KEY,
    mo_ta TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE quyen_han (
    ten_quyen_han VARCHAR(50) PRIMARY KEY,
    mo_ta TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE nguoi_dung_vai_tro (
    ma_nguoi_dung VARCHAR(36) NOT NULL,
    ten_vai_tro VARCHAR(50) NOT NULL,
    PRIMARY KEY (ma_nguoi_dung, ten_vai_tro),
    CONSTRAINT fk_nguoi_dung_vai_tro_nguoi_dung FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung),
    CONSTRAINT fk_nguoi_dung_vai_tro_vai_tro FOREIGN KEY (ten_vai_tro) REFERENCES vai_tro(ten_vai_tro)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE vai_tro_quyen_han (
    ten_vai_tro VARCHAR(50) NOT NULL,
    ten_quyen_han VARCHAR(50) NOT NULL,
    PRIMARY KEY (ten_vai_tro, ten_quyen_han),
    CONSTRAINT fk_vai_tro_quyen_han_vai_tro FOREIGN KEY (ten_vai_tro) REFERENCES vai_tro(ten_vai_tro),
    CONSTRAINT fk_vai_tro_quyen_han_quyen_han FOREIGN KEY (ten_quyen_han) REFERENCES quyen_han(ten_quyen_han)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE token_da_huy (
    ma_token VARCHAR(255) PRIMARY KEY,
    thoi_gian_het_han DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE dia_chi (
    ma_dia_chi VARCHAR(36) PRIMARY KEY,
    ma_nguoi_dung VARCHAR(36) NOT NULL,
    so_nha VARCHAR(50) NOT NULL,
    phuong_xa VARCHAR(255) NOT NULL,
    quan_huyen VARCHAR(255) NOT NULL,
    tinh_thanh VARCHAR(255) NOT NULL,
    CONSTRAINT fk_dia_chi_nguoi_dung FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE loai_hang (
    ma_loai_hang VARCHAR(36) PRIMARY KEY,
    ten_loai_hang VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;


CREATE TABLE san_pham (
    ma_san_pham VARCHAR(36) PRIMARY KEY,
    ten_san_pham VARCHAR(255) NOT NULL,
    mo_ta_san_pham TEXT NOT NULL,
    gia_san_pham DECIMAL(10, 2) NOT NULL,
    so_luong_ton INT NOT NULL,
    ma_loai_hang VARCHAR(36) NOT NULL,
    thoi_gian_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_san_pham_loai_hang FOREIGN KEY (ma_loai_hang) REFERENCES loai_hang(ma_loai_hang)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE hinh_anh_san_pham (
    ma_hinh_anh VARCHAR(36) PRIMARY KEY,
    ma_san_pham VARCHAR(36) NOT NULL,
    CONSTRAINT fk_hinh_anh_san_pham FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE don_hang (
    ma_don_hang VARCHAR(36) PRIMARY KEY,
    ma_nguoi_dung VARCHAR(36) NOT NULL,
    ma_dia_chi VARCHAR(36) NOT NULL,
    tong_tien DECIMAL(10,2) NOT NULL,
    trang_thai ENUM('PENDING', 'PROCESSING', 'SHIPPED', 'DELIVERED', 'CANCELLED') DEFAULT 'PENDING',
    thoi_gian_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_don_hang_nguoi_dung FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung),
    CONSTRAINT fk_don_hang_dia_chi FOREIGN KEY (ma_dia_chi) REFERENCES dia_chi(ma_dia_chi)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE chi_tiet_don_hang (
    ma_ct_don_hang VARCHAR(36) PRIMARY KEY,
    ma_don_hang VARCHAR(36) NOT NULL,
    ma_san_pham VARCHAR(36) NOT NULL,
    so_luong INT NOT NULL,
    gia DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_ct_don_hang_don_hang FOREIGN KEY (ma_don_hang) REFERENCES don_hang(ma_don_hang),
    CONSTRAINT fk_ct_don_hang_san_pham FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE thanh_toan (
    ma_thanh_toan VARCHAR(36) PRIMARY KEY,
    ma_don_hang VARCHAR(36) NOT NULL,
    phuong_thuc VARCHAR(50) NOT NULL,
    trang_thai ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    ngay_thanh_toan DATETIME,
    CONSTRAINT fk_thanh_toan_don_hang FOREIGN KEY (ma_don_hang) REFERENCES don_hang(ma_don_hang)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE danh_gia (
    ma_danh_gia VARCHAR(36) PRIMARY KEY,
    ma_nguoi_dung VARCHAR(36) NOT NULL,
    ma_san_pham VARCHAR(36) NOT NULL,
    diem_danh_gia INT NOT NULL,
    binh_luan TEXT,
    thoi_gian_tao DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_danh_gia_nguoi_dung FOREIGN KEY (ma_nguoi_dung) REFERENCES nguoi_dung(ma_nguoi_dung),
    CONSTRAINT fk_danh_gia_san_pham FOREIGN KEY (ma_san_pham) REFERENCES san_pham(ma_san_pham),
    CONSTRAINT check_diem_danh_gia CHECK (diem_danh_gia BETWEEN 1 AND 5)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

-- Đã tạo trực tiếp trong source khi chay lần đầu
-- 2. Insert các vai trò cơ bản theo mô hình RBAC
-- INSERT IGNORE INTO vai_tro (ten_vai_tro, mo_ta) VALUES 
-- ('USER', 'Người dùng - Có thể mua hàng, quản lý đơn hàng và thông tin cá nhân'),
-- ('SHOP', 'Cửa hàng - Có thể quản lý sản phẩm, xử lý đơn hàng'),
-- ('ADMIN', 'Quản trị viên - Có toàn quyền quản lý hệ thống');

-- 3. Insert các permissions
INSERT IGNORE INTO quyen_han (ten_quyen_han, mo_ta) VALUES
-- User Management Permissions
('USER_CREATE', 'Quyền tạo tài khoản người dùng mới'),
('USER_READ', 'Quyền xem thông tin người dùng'),
('USER_UPDATE', 'Quyền cập nhật thông tin người dùng'),
('USER_DELETE', 'Quyền xóa tài khoản người dùng'),
('USER_READ_ALL', 'Quyền xem tất cả người dùng trong hệ thống'),
-- Role Management Permissions  
('ROLE_CREATE', 'Quyền tạo vai trò mới'),
('ROLE_READ', 'Quyền xem thông tin vai trò'),
('ROLE_UPDATE', 'Quyền cập nhật vai trò'),
('ROLE_DELETE', 'Quyền xóa vai trò'),
-- Permission Management Permissions
('PERMISSION_CREATE', 'Quyền tạo quyền hạn mới'),
('PERMISSION_READ', 'Quyền xem danh sách quyền hạn'),
('PERMISSION_DELETE', 'Quyền xóa quyền hạn'),
-- Address Management Permissions
('ADDRESS_CREATE', 'Quyền tạo địa chỉ mới'),
('ADDRESS_READ', 'Quyền xem thông tin địa chỉ'),
('ADDRESS_UPDATE', 'Quyền cập nhật địa chỉ'),
('ADDRESS_DELETE', 'Quyền xóa địa chỉ'),
('ADDRESS_READ_ALL', 'Quyền xem tất cả địa chỉ trong hệ thống'),
-- Order Management Permissions
('ORDER_CREATE', 'Quyền tạo đơn hàng mới'),
('ORDER_READ', 'Quyền xem thông tin đơn hàng'),
('ORDER_UPDATE', 'Quyền cập nhật đơn hàng'),
('ORDER_DELETE', 'Quyền xóa đơn hàng'),
('ORDER_READ_ALL', 'Quyền xem tất cả đơn hàng trong hệ thống'),
('ORDER_APPROVE', 'Quyền duyệt đơn hàng'),
('ORDER_CANCEL', 'Quyền hủy đơn hàng');


-- 4. Gán permissions cho từng vai trò theo mô hình RBAC

-- USER Role Permissions
INSERT IGNORE INTO vai_tro_quyen_han (ten_vai_tro, ten_quyen_han) VALUES
('USER', 'USER_READ'),
('USER', 'USER_UPDATE'),
('USER', 'ADDRESS_CREATE'),
('USER', 'ADDRESS_READ'),
('USER', 'ADDRESS_UPDATE'),
('USER', 'ADDRESS_DELETE'),
('USER', 'ORDER_CREATE'),
('USER', 'ORDER_READ'),
('USER', 'ORDER_CANCEL');

-- SHOP Role Permissions
INSERT IGNORE INTO vai_tro_quyen_han (ten_vai_tro, ten_quyen_han) VALUES
('SHOP', 'USER_READ'),
('SHOP', 'USER_UPDATE'),
('SHOP', 'ORDER_READ'),
('SHOP', 'ORDER_UPDATE'),
('SHOP', 'ORDER_READ_ALL'),
('SHOP', 'ORDER_APPROVE'),
('SHOP', 'ADDRESS_READ');

-- ADMIN Role Permissions (toàn quyền)
INSERT IGNORE INTO vai_tro_quyen_han (ten_vai_tro, ten_quyen_han) VALUES
('ADMIN', 'USER_CREATE'),
('ADMIN', 'USER_READ'),
('ADMIN', 'USER_UPDATE'),
('ADMIN', 'USER_DELETE'),
('ADMIN', 'USER_READ_ALL'),
('ADMIN', 'ROLE_CREATE'),
('ADMIN', 'ROLE_READ'),
('ADMIN', 'ROLE_UPDATE'),
('ADMIN', 'ROLE_DELETE'),
('ADMIN', 'PERMISSION_CREATE'),
('ADMIN', 'PERMISSION_READ'),
('ADMIN', 'PERMISSION_DELETE'),
('ADMIN', 'ADDRESS_CREATE'),
('ADMIN', 'ADDRESS_READ'),
('ADMIN', 'ADDRESS_UPDATE'),
('ADMIN', 'ADDRESS_DELETE'),
('ADMIN', 'ADDRESS_READ_ALL'),
('ADMIN', 'ORDER_CREATE'),
('ADMIN', 'ORDER_READ'),
('ADMIN', 'ORDER_UPDATE'),
('ADMIN', 'ORDER_DELETE'),
('ADMIN', 'ORDER_READ_ALL'),
('ADMIN', 'ORDER_APPROVE'),
('ADMIN', 'ORDER_CANCEL');

-- Hiển thị kết quả
SELECT 'Setup permissions completed!' as status;
SELECT COUNT(*) as total_permissions FROM quyen_han;
SELECT COUNT(*) as total_roles FROM vai_tro;
SELECT COUNT(*) as total_role_permissions FROM vai_tro_quyen_han;


-- -- Thêm ràng buộc cho số điện thoại
-- ALTER TABLE nguoi_dung ADD CONSTRAINT check_phone 
-- CHECK (so_dien_thoai REGEXP '^(0|\\+?84)(3|5|7|8|9)[0-9]{8}$');

-- -- Thêm ràng buộc cho email
-- ALTER TABLE nguoi_dung ADD CONSTRAINT check_email 
-- CHECK (email REGEXP '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$');

-- -- Thêm ràng buộc cho giá trị tiền
-- ALTER TABLE don_hang ADD CONSTRAINT check_total_amount 
-- CHECK (tong_tien > 0);
