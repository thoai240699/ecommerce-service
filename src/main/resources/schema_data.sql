CREATE DATABASE IF NOT EXISTS ecommerce_service CHARACTER SET utf8mb4 COLLATE utf8mb4_vi_0900_ai_ci;
USE ecommerce_service;
-- Thiết lập charset cho tiếng Việt
SET NAMES 'utf8mb4';

-- Tạo bảng
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
    trang_thai ENUM('Chờ xử lý', 'Đang xử lý', 'Đã gửi', 'Đã giao', 'Đã hủy') DEFAULT 'Chờ xử lý',
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
    trang_thai ENUM('Chờ xử lý', 'Hoàn tất', 'Thất bại') DEFAULT 'Chờ xử lý',
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

