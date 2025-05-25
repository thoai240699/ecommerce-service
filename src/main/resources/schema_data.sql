CREATE DATABASE IF NOT EXISTS ecommerce_service CHARACTER SET utf8mb4 COLLATE utf8mb4_vi_0900_ai_ci;
USE ecommerce_service;
-- Thiết lập charset cho tiếng Việt
SET NAMES 'utf8mb4';

-- Xóa bảng nếu đã tồn tại
DROP TABLE IF EXISTS danh_gia;
DROP TABLE IF EXISTS thanh_toan;
DROP TABLE IF EXISTS chi_tiet_don_hang;
DROP TABLE IF EXISTS don_hang;
DROP TABLE IF EXISTS hinh_anh_san_pham;
DROP TABLE IF EXISTS san_pham;
DROP TABLE IF EXISTS dia_chi;
DROP TABLE IF EXISTS nguoi_dung;
DROP TABLE IF EXISTS loai_hang;

-- Tạo bảng
CREATE TABLE loai_hang (
    ma_loai_hang VARCHAR(36) PRIMARY KEY,
    ten_loai_hang VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_vi_0900_ai_ci;

CREATE TABLE nguoi_dung (
    ma_nguoi_dung VARCHAR(36) PRIMARY KEY,
    ten_dang_nhap VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    ho_ten VARCHAR(255),
    so_dien_thoai VARCHAR(20) NOT NULL,
    vai_tro ENUM('Quản trị viên', 'Nhân viên', 'Khách hàng') DEFAULT 'Khách hàng',
    thoi_gian_tao DATETIME DEFAULT CURRENT_TIMESTAMP
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

-- Dữ liệu mẫu

-- Bảng loại hàng
INSERT INTO loai_hang (ma_loai_hang, ten_loai_hang) VALUES
('LH001', 'Điện thoại di động'),
('LH002', 'Laptop'),
('LH003', 'Thời trang nữ'),
('LH004', 'Thời trang nam'),
('LH005', 'Mỹ phẩm'),
('LH006', 'Đồ gia dụng'),
('LH007', 'Sách'),
('LH008', 'Đồng hồ'),
('LH009', 'Giày dép'),
('LH010', 'Phụ kiện điện tử'),
('LH011', 'Máy tính bảng'),
('LH012', 'Túi xách'),
('LH013', 'Thiết bị mạng'),
('LH014', 'Đồ chơi'),
('LH015', 'Thực phẩm chức năng'),
('LH016', 'Đồ dùng học tập'),
('LH017', 'Máy ảnh'),
('LH018', 'Thiết bị nhà bếp'),
('LH019', 'Thiết bị chăm sóc sức khỏe'),
('LH020', 'Đèn & thiết bị chiếu sáng');

-- Bảng người dùng
INSERT INTO nguoi_dung (ma_nguoi_dung, ten_dang_nhap, mat_khau, email, ho_ten, so_dien_thoai, vai_tro) VALUES
('ND001', 'nguyenvana', '$2a$10$abc...', 'nguyenvana@gmail.com', 'Nguyễn Văn A', '0912345678', 'Khách hàng'),
('ND002', 'tranthib', '$2a$10$def...', 'tranthib@gmail.com', 'Trần Thị B', '0987654321', 'Khách hàng'),
('ND003', 'leminhc', '$2a$10$ghi...', 'leminhc@gmail.com', 'Lê Minh C', '0934567890', 'Khách hàng'),
('ND004', 'phamthid', '$2a$10$jkl...', 'phamthid@gmail.com', 'Phạm Thị D', '0945678901', 'Khách hàng'),
('ND005', 'buitranh', '$2a$10$mno...', 'buitranh@gmail.com', 'Bùi Trần H', '0956789012', 'Khách hàng'),
('ND006', 'ngothik', '$2a$10$pqr...', 'ngothik@gmail.com', 'Ngô Thị K', '0967890123', 'Khách hàng'),
('ND007', 'phamvanl', '$2a$10$stu...', 'phamvanl@gmail.com', 'Phạm Văn L', '0978901234', 'Khách hàng'),
('ND008', 'dinhthim', '$2a$10$vwx...', 'dinhthim@gmail.com', 'Đinh Thị M', '0989012345', 'Khách hàng'),
('ND009', 'hoangvann', '$2a$10$yz1...', 'hoangvann@gmail.com', 'Hoàng Văn N', '0990123456', 'Khách hàng'),
('ND010', 'vuquano', '$2a$10$234...', 'vuquano@gmail.com', 'Vũ Quân O', '0901234567', 'Khách hàng'),
('ND011', 'admin', '$2a$10$admin...', 'admin@shopee.vn', 'Quản trị Shopee', '0999999999', 'Quản trị viên'),
('ND012', 'staff1', '$2a$10$staff1...', 'staff1@shopee.vn', 'Nguyễn Thị Nhân Viên', '0911111111', 'Nhân viên'),
('ND013', 'staff2', '$2a$10$staff2...', 'staff2@shopee.vn', 'Trần Văn Nhân Viên', '0922222222', 'Nhân viên'),
('ND014', 'nguyenthih', '$2a$10$h14...', 'nguyenthih@gmail.com', 'Nguyễn Thị H', '0933333333', 'Khách hàng'),
('ND015', 'lethik', '$2a$10$k15...', 'lethik@gmail.com', 'Lê Thị K', '0944444444', 'Khách hàng'),
('ND016', 'phamthil', '$2a$10$l16...', 'phamthil@gmail.com', 'Phạm Thị L', '0955555555', 'Khách hàng'),
('ND017', 'tranminhm', '$2a$10$m17...', 'tranminhm@gmail.com', 'Trần Minh M', '0966666666', 'Khách hàng'),
('ND018', 'nguyenvann', '$2a$10$n18...', 'nguyenvann@gmail.com', 'Nguyễn Văn N', '0977777777', 'Khách hàng'),
('ND019', 'buitranp', '$2a$10$p19...', 'buitranp@gmail.com', 'Bùi Trần P', '0988888888', 'Khách hàng'),
('ND020', 'ngothiq', '$2a$10$q20...', 'ngothiq@gmail.com', 'Ngô Thị Q', '0999999998', 'Khách hàng'),
('ND021', 'phamvanr', '$2a$10$r21...', 'phamvanr@gmail.com', 'Phạm Văn R', '0900000001', 'Khách hàng'),
('ND022', 'dinhthis', '$2a$10$s22...', 'dinhthis@gmail.com', 'Đinh Thị S', '0900000002', 'Khách hàng'),
('ND023', 'hoangvant', '$2a$10$t23...', 'hoangvant@gmail.com', 'Hoàng Văn T', '0900000003', 'Khách hàng'),
('ND024', 'vuquanu', '$2a$10$u24...', 'vuquanu@gmail.com', 'Vũ Quân U', '0900000004', 'Khách hàng'),
('ND025', 'nguyenvanv', '$2a$10$v25...', 'nguyenvanv@gmail.com', 'Nguyễn Văn V', '0900000005', 'Khách hàng'),
('ND026', 'tranthiw', '$2a$10$w26...', 'tranthiw@gmail.com', 'Trần Thị W', '0900000006', 'Khách hàng'),
('ND027', 'leminhx', '$2a$10$x27...', 'leminhx@gmail.com', 'Lê Minh X', '0900000007', 'Khách hàng'),
('ND028', 'phamthiy', '$2a$10$y28...', 'phamthiy@gmail.com', 'Phạm Thị Y', '0900000008', 'Khách hàng'),
('ND029', 'buitranz', '$2a$10$z29...', 'buitranz@gmail.com', 'Bùi Trần Z', '0900000009', 'Khách hàng'),
('ND030', 'ngothiaa', '$2a$10$aa30...', 'ngothiaa@gmail.com', 'Ngô Thị AA', '0900000010', 'Khách hàng');

-- Bảng địa chỉ
INSERT INTO dia_chi (ma_dia_chi, ma_nguoi_dung, so_nha, phuong_xa, quan_huyen, tinh_thanh) VALUES
('DC001', 'ND001', '123', 'Phường Bến Nghé', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC002', 'ND002', '456', 'Phường Đa Kao', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC003', 'ND003', '789', 'Phường Bến Thành', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC004', 'ND004', '321', 'Phường 2', 'Quận 3', 'TP. Hồ Chí Minh'),
('DC005', 'ND005', '654', 'Phường 4', 'Quận 5', 'TP. Hồ Chí Minh'),
('DC006', 'ND006', '987', 'Phường 7', 'Quận 8', 'TP. Hồ Chí Minh'),
('DC007', 'ND007', '147', 'Phường 12', 'Quận Gò Vấp', 'TP. Hồ Chí Minh'),
('DC008', 'ND008', '258', 'Phường 9', 'Quận Phú Nhuận', 'TP. Hồ Chí Minh'),
('DC009', 'ND009', '369', 'Phường 10', 'Quận 10', 'TP. Hồ Chí Minh'),
('DC010', 'ND010', '159', 'Phường 5', 'Quận 11', 'TP. Hồ Chí Minh'),
('DC011', 'ND011', '753', 'Phường 6', 'Quận 6', 'TP. Hồ Chí Minh'),
('DC012', 'ND012', '852', 'Phường 7', 'Quận 7', 'TP. Hồ Chí Minh'),
('DC013', 'ND013', '951', 'Phường 8', 'Quận 8', 'TP. Hồ Chí Minh'),
('DC014', 'ND014', '357', 'Phường 3', 'Quận 3', 'TP. Hồ Chí Minh'),
('DC015', 'ND015', '258', 'Phường 4', 'Quận 4', 'TP. Hồ Chí Minh'),
('DC016', 'ND016', '159', 'Phường 5', 'Quận 5', 'TP. Hồ Chí Minh'),
('DC017', 'ND017', '357', 'Phường 6', 'Quận 6', 'TP. Hồ Chí Minh'),
('DC018', 'ND018', '456', 'Phường 7', 'Quận 7', 'TP. Hồ Chí Minh'),
('DC019', 'ND019', '654', 'Phường 8', 'Quận 8', 'TP. Hồ Chí Minh'),
('DC020', 'ND020', '753', 'Phường 9', 'Quận 9', 'TP. Hồ Chí Minh'),
('DC021', 'ND021', '951', 'Phường 10', 'Quận 10', 'TP. Hồ Chí Minh'),
('DC022', 'ND022', '357', 'Phường 11', 'Quận 11', 'TP. Hồ Chí Minh'),
('DC023', 'ND023', '258', 'Phường 12', 'Quận 12', 'TP. Hồ Chí Minh'),
('DC024', 'ND024', '159', 'Phường 13', 'Quận Bình Thạnh', 'TP. Hồ Chí Minh'),
('DC025', 'ND025', '357', 'Phường 14', 'Quận Bình Tân', 'TP. Hồ Chí Minh'),
('DC026', 'ND026', '456', 'Phường 15', 'Quận Tân Bình', 'TP. Hồ Chí Minh'),
('DC027', 'ND027', '654', 'Phường 16', 'Quận Tân Phú', 'TP. Hồ Chí Minh'),
('DC028', 'ND028', '753', 'Phường 17', 'Quận Thủ Đức', 'TP. Hồ Chí Minh'),
('DC029', 'ND029', '951', 'Phường 18', 'Quận 9', 'TP. Hồ Chí Minh'),
('DC030', 'ND030', '357', 'Phường 19', 'Quận 2', 'TP. Hồ Chí Minh');

-- Bảng sản phẩm
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta_san_pham, gia_san_pham, so_luong_ton, ma_loai_hang, thoi_gian_tao) VALUES
('SP001', 'iPhone 15 Pro Max', 'Điện thoại Apple iPhone 15 Pro Max 256GB', 34990000, 100, 'LH001', NOW()),
('SP002', 'Samsung Galaxy S23 Ultra', 'Điện thoại Samsung Galaxy S23 Ultra 256GB', 26990000, 80, 'LH001', NOW()),
('SP003', 'MacBook Pro M2', 'Laptop MacBook Pro M2 13 inch', 32990000, 50, 'LH002', NOW()),
('SP004', 'Dell XPS 13', 'Laptop Dell XPS 13 2023', 28990000, 40, 'LH002', NOW()),
('SP005', 'Áo thun nữ Uniqlo', 'Áo thun nữ Uniqlo chính hãng', 299000, 200, 'LH003', NOW()),
('SP006', 'Váy nữ Zara', 'Váy nữ Zara mùa hè', 499000, 150, 'LH003', NOW()),
('SP007', 'Áo sơ mi nam Owen', 'Áo sơ mi nam Owen cao cấp', 399000, 120, 'LH004', NOW()),
('SP008', 'Quần jeans nam Levi''s', 'Quần jeans nam Levi''s chính hãng', 899000, 100, 'LH004', NOW()),
('SP009', 'Son môi 3CE', 'Son môi 3CE màu đỏ cam', 350000, 150, 'LH005', NOW()),
('SP010', 'Kem chống nắng Anessa', 'Kem chống nắng Anessa SPF50+', 550000, 130, 'LH005', NOW()),
('SP011', 'Nồi chiên không dầu Lock&Lock', 'Nồi chiên không dầu 5L Lock&Lock', 1990000, 50, 'LH006', NOW()),
('SP012', 'Máy hút bụi Xiaomi', 'Máy hút bụi cầm tay Xiaomi', 1590000, 60, 'LH006', NOW()),
('SP013', 'Sách Đắc Nhân Tâm', 'Sách phát triển bản thân Đắc Nhân Tâm', 89000, 300, 'LH007', NOW()),
('SP014', 'Sách Nhà Giả Kim', 'Sách tiểu thuyết Nhà Giả Kim', 99000, 250, 'LH007', NOW()),
('SP015', 'Đồng hồ Casio MTP', 'Đồng hồ nam Casio MTP chính hãng', 1299000, 70, 'LH008', NOW()),
('SP016', 'Đồng hồ nữ Daniel Wellington', 'Đồng hồ nữ Daniel Wellington dây da', 2999000, 60, 'LH008', NOW()),
('SP017', 'Giày sneaker Adidas', 'Giày sneaker Adidas chính hãng', 1999000, 90, 'LH009', NOW()),
('SP018', 'Giày cao gót Vascara', 'Giày cao gót nữ Vascara', 899000, 80, 'LH009', NOW()),
('SP019', 'Tai nghe AirPods Pro', 'Tai nghe không dây Apple AirPods Pro', 4990000, 110, 'LH010', NOW()),
('SP020', 'Sạc dự phòng Anker', 'Sạc dự phòng Anker 10000mAh', 590000, 140, 'LH010', NOW()),
('SP021', 'iPad Pro 12.9', 'Máy tính bảng iPad Pro 12.9 inch 2023', 25990000, 30, 'LH011', NOW()),
('SP022', 'Túi xách Charles & Keith', 'Túi xách nữ Charles & Keith', 1599000, 70, 'LH012', NOW()),
('SP023', 'Router WiFi TP-Link', 'Router WiFi TP-Link Archer C6', 690000, 60, 'LH013', NOW()),
('SP024', 'Bộ xếp hình Lego', 'Đồ chơi xếp hình Lego Classic', 499000, 90, 'LH014', NOW()),
('SP025', 'Viên uống DHC Vitamin C', 'Thực phẩm chức năng DHC Vitamin C', 129000, 200, 'LH015', NOW()),
('SP026', 'Bút bi Thiên Long', 'Bút bi Thiên Long TL-027', 5000, 500, 'LH016', NOW()),
('SP027', 'Máy ảnh Canon EOS M50', 'Máy ảnh Canon EOS M50 Mark II', 15990000, 20, 'LH017', NOW()),
('SP028', 'Bếp từ Sunhouse', 'Bếp từ đôi Sunhouse SHB9106', 2990000, 40, 'LH018', NOW()),
('SP029', 'Máy đo huyết áp Omron', 'Máy đo huyết áp điện tử Omron HEM-7121', 890000, 50, 'LH019', NOW()),
('SP030', 'Đèn bàn LED Rạng Đông', 'Đèn bàn LED Rạng Đông RD-RL-24', 199000, 100, 'LH020', NOW());

-- Bảng hình ảnh sản phẩm
INSERT INTO hinh_anh_san_pham (ma_hinh_anh, ma_san_pham) VALUES
('HA001', 'SP001'),('HA002', 'SP001'),('HA003', 'SP002'),('HA004', 'SP003'),('HA005', 'SP004'),('HA006', 'SP005'),('HA007', 'SP006'),('HA008', 'SP007'),('HA009', 'SP008'),('HA010', 'SP009'),('HA011', 'SP010'),('HA012', 'SP011'),('HA013', 'SP012'),('HA014', 'SP013'),('HA015', 'SP014'),('HA016', 'SP015'),('HA017', 'SP016'),('HA018', 'SP017'),('HA019', 'SP018'),('HA020', 'SP019'),('HA021', 'SP020'),('HA022', 'SP021'),('HA023', 'SP022'),('HA024', 'SP023'),('HA025', 'SP024'),('HA026', 'SP025'),('HA027', 'SP026'),('HA028', 'SP027'),('HA029', 'SP028'),('HA030', 'SP029');

-- Bảng đơn hàng
INSERT INTO don_hang (ma_don_hang, ma_nguoi_dung, ma_dia_chi, tong_tien, trang_thai, thoi_gian_tao) VALUES
('DH001', 'ND001', 'DC001', 34990000, 'Đã giao', NOW()),
('DH002', 'ND002', 'DC002', 299000, 'Đang xử lý', NOW()),
('DH003', 'ND003', 'DC003', 499000, 'Chờ xử lý', NOW()),
('DH004', 'ND004', 'DC004', 399000, 'Đã giao', NOW()),
('DH005', 'ND005', 'DC005', 350000, 'Đã giao', NOW()),
('DH006', 'ND006', 'DC006', 550000, 'Đang xử lý', NOW()),
('DH007', 'ND007', 'DC007', 1990000, 'Chờ xử lý', NOW()),
('DH008', 'ND008', 'DC008', 1590000, 'Đã giao', NOW()),
('DH009', 'ND009', 'DC009', 89000, 'Đã giao', NOW()),
('DH010', 'ND010', 'DC010', 99000, 'Đang xử lý', NOW()),
('DH011', 'ND011', 'DC011', 1299000, 'Chờ xử lý', NOW()),
('DH012', 'ND012', 'DC012', 2999000, 'Đã giao', NOW()),
('DH013', 'ND013', 'DC013', 1999000, 'Đã giao', NOW()),
('DH014', 'ND014', 'DC014', 899000, 'Đang xử lý', NOW()),
('DH015', 'ND015', 'DC015', 4990000, 'Chờ xử lý', NOW()),
('DH016', 'ND016', 'DC016', 590000, 'Đã giao', NOW()),
('DH017', 'ND017', 'DC017', 25990000, 'Đã giao', NOW()),
('DH018', 'ND018', 'DC018', 1599000, 'Đang xử lý', NOW()),
('DH019', 'ND019', 'DC019', 690000, 'Chờ xử lý', NOW()),
('DH020', 'ND020', 'DC020', 499000, 'Đã giao', NOW()),
('DH021', 'ND021', 'DC021', 129000, 'Đã giao', NOW()),
('DH022', 'ND022', 'DC022', 5000, 'Đang xử lý', NOW()),
('DH023', 'ND023', 'DC023', 15990000, 'Chờ xử lý', NOW()),
('DH024', 'ND024', 'DC024', 2990000, 'Đã giao', NOW()),
('DH025', 'ND025', 'DC025', 890000, 'Đã giao', NOW()),
('DH026', 'ND026', 'DC026', 199000, 'Đang xử lý', NOW()),
('DH027', 'ND027', 'DC027', 15990000, 'Chờ xử lý', NOW()),
('DH028', 'ND028', 'DC028', 299000, 'Đã giao', NOW()),
('DH029', 'ND029', 'DC029', 890000, 'Đã giao', NOW()),
('DH030', 'ND030', 'DC030', 199000, 'Đang xử lý', NOW());

-- Bảng chi tiết đơn hàng
INSERT INTO chi_tiet_don_hang (ma_ct_don_hang, ma_don_hang, ma_san_pham, so_luong, gia) VALUES
('CT001', 'DH001', 'SP001', 1, 34990000),
('CT002', 'DH002', 'SP005', 1, 299000),
('CT003', 'DH003', 'SP006', 1, 499000),
('CT004', 'DH004', 'SP007', 1, 399000),
('CT005', 'DH005', 'SP009', 1, 350000),
('CT006', 'DH006', 'SP010', 1, 550000),
('CT007', 'DH007', 'SP011', 1, 1990000),
('CT008', 'DH008', 'SP012', 1, 1590000),
('CT009', 'DH009', 'SP013', 1, 89000),
('CT010', 'DH010', 'SP014', 1, 99000),
('CT011', 'DH011', 'SP015', 1, 1299000),
('CT012', 'DH012', 'SP016', 1, 2999000),
('CT013', 'DH013', 'SP017', 1, 1999000),
('CT014', 'DH014', 'SP018', 1, 899000),
('CT015', 'DH015', 'SP019', 1, 4990000),
('CT016', 'DH016', 'SP020', 1, 590000),
('CT017', 'DH017', 'SP021', 1, 25990000),
('CT018', 'DH018', 'SP022', 1, 1599000),
('CT019', 'DH019', 'SP023', 1, 690000),
('CT020', 'DH020', 'SP024', 1, 499000),
('CT021', 'DH021', 'SP025', 1, 129000),
('CT022', 'DH022', 'SP026', 1, 5000),
('CT023', 'DH023', 'SP027', 1, 15990000),
('CT024', 'DH024', 'SP028', 1, 2990000),
('CT025', 'DH025', 'SP029', 1, 890000),
('CT026', 'DH026', 'SP030', 1, 199000),
('CT027', 'DH027', 'SP027', 1, 15990000),
('CT028', 'DH028', 'SP005', 1, 299000),
('CT029', 'DH029', 'SP029', 1, 890000),
('CT030', 'DH030', 'SP030', 1, 199000);

-- Bảng thanh toán
INSERT INTO thanh_toan (ma_thanh_toan, ma_don_hang, phuong_thuc, trang_thai, ngay_thanh_toan) VALUES
('TT001', 'DH001', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT002', 'DH002', 'Ví ShopeePay', 'Chờ xử lý', NULL),
('TT003', 'DH003', 'Chuyển khoản', 'Chờ xử lý', NULL),
('TT004', 'DH004', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT005', 'DH005', 'Ví ShopeePay', 'Hoàn tất', NOW()),
('TT006', 'DH006', 'Chuyển khoản', 'Chờ xử lý', NULL),
('TT007', 'DH007', 'Thẻ tín dụng', 'Chờ xử lý', NULL),
('TT008', 'DH008', 'Ví ShopeePay', 'Hoàn tất', NOW()),
('TT009', 'DH009', 'Chuyển khoản', 'Hoàn tất', NOW()),
('TT010', 'DH010', 'Thẻ tín dụng', 'Chờ xử lý', NULL),
('TT011', 'DH011', 'Ví ShopeePay', 'Chờ xử lý', NULL),
('TT012', 'DH012', 'Chuyển khoản', 'Hoàn tất', NOW()),
('TT013', 'DH013', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT014', 'DH014', 'Ví ShopeePay', 'Chờ xử lý', NULL),
('TT015', 'DH015', 'Chuyển khoản', 'Chờ xử lý', NULL),
('TT016', 'DH016', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT017', 'DH017', 'Ví ShopeePay', 'Hoàn tất', NOW()),
('TT018', 'DH018', 'Chuyển khoản', 'Chờ xử lý', NULL),
('TT019', 'DH019', 'Thẻ tín dụng', 'Chờ xử lý', NULL),
('TT020', 'DH020', 'Ví ShopeePay', 'Hoàn tất', NOW()),
('TT021', 'DH021', 'Chuyển khoản', 'Hoàn tất', NOW()),
('TT022', 'DH022', 'Thẻ tín dụng', 'Chờ xử lý', NULL),
('TT023', 'DH023', 'Ví ShopeePay', 'Chờ xử lý', NULL),
('TT024', 'DH024', 'Chuyển khoản', 'Hoàn tất', NOW()),
('TT025', 'DH025', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT026', 'DH026', 'Ví ShopeePay', 'Chờ xử lý', NULL),
('TT027', 'DH027', 'Chuyển khoản', 'Chờ xử lý', NULL),
('TT028', 'DH028', 'Thẻ tín dụng', 'Hoàn tất', NOW()),
('TT029', 'DH029', 'Ví ShopeePay', 'Hoàn tất', NOW()),
('TT030', 'DH030', 'Chuyển khoản', 'Chờ xử lý', NULL);

-- Bảng đánh giá
INSERT INTO danh_gia (ma_danh_gia, ma_nguoi_dung, ma_san_pham, diem_danh_gia, binh_luan, thoi_gian_tao) VALUES
('DG001', 'ND001', 'SP001', 5, 'Sản phẩm chính hãng, giao hàng nhanh!', NOW()),
('DG002', 'ND002', 'SP005', 4, 'Áo đẹp, chất vải tốt.', NOW()),
('DG003', 'ND003', 'SP003', 5, 'Laptop mạnh, màn hình đẹp.', NOW()),
('DG004', 'ND004', 'SP009', 4, 'Son môi màu đẹp, lên màu chuẩn.', NOW()),
('DG005', 'ND005', 'SP011', 5, 'Nồi chiên tiện lợi, dễ sử dụng.', NOW()),
('DG006', 'ND006', 'SP013', 5, 'Sách hay, truyền cảm hứng.', NOW()),
('DG007', 'ND007', 'SP015', 4, 'Đồng hồ đẹp, giá hợp lý.', NOW()),
('DG008', 'ND008', 'SP017', 5, 'Giày đi êm, chất lượng tốt.', NOW()),
('DG009', 'ND009', 'SP019', 5, 'Tai nghe nghe nhạc hay, pin trâu.', NOW()),
('DG010', 'ND010', 'SP021', 5, 'iPad Pro tuyệt vời cho công việc.', NOW()),
('DG011', 'ND011', 'SP022', 4, 'Túi xách đẹp, giao hàng nhanh.', NOW()),
('DG012', 'ND012', 'SP023', 5, 'Router sóng mạnh, dễ cài đặt.', NOW()),
('DG013', 'ND013', 'SP025', 5, 'Viên uống DHC tốt cho sức khỏe.', NOW()),
('DG014', 'ND014', 'SP027', 5, 'Máy ảnh chụp đẹp, dễ dùng.', NOW()),
('DG015', 'ND015', 'SP029', 4, 'Máy đo huyết áp chính xác.', NOW()),
('DG016', 'ND016', 'SP030', 5, 'Đèn bàn sáng, tiết kiệm điện.', NOW()),
('DG017', 'ND017', 'SP002', 5, 'Samsung S23 Ultra tuyệt vời.', NOW()),
('DG018', 'ND018', 'SP004', 4, 'Dell XPS mỏng nhẹ, pin tốt.', NOW()),
('DG019', 'ND019', 'SP006', 5, 'Váy Zara đẹp, chất vải mát.', NOW()),
('DG020', 'ND020', 'SP008', 5, 'Quần jeans Levi''s bền, đẹp.', NOW()),
('DG021', 'ND021', 'SP010', 5, 'Kem chống nắng Anessa tốt.', NOW()),
('DG022', 'ND022', 'SP012', 4, 'Máy hút bụi Xiaomi tiện lợi.', NOW()),
('DG023', 'ND023', 'SP014', 5, 'Nhà Giả Kim là cuốn sách hay.', NOW()),
('DG024', 'ND024', 'SP016', 5, 'Đồng hồ DW sang trọng.', NOW()),
('DG025', 'ND025', 'SP018', 4, 'Giày cao gót Vascara đẹp.', NOW()),
('DG026', 'ND026', 'SP020', 5, 'Sạc dự phòng Anker nhỏ gọn.', NOW()),
('DG027', 'ND027', 'SP024', 5, 'Lego Classic nhiều chi tiết.', NOW()),
('DG028', 'ND028', 'SP026', 5, 'Bút bi Thiên Long viết mượt.', NOW()),
('DG029', 'ND029', 'SP028', 4, 'Bếp từ Sunhouse nấu nhanh.', NOW()),
('DG030', 'ND030', 'SP018', 5, 'Giày cao gót Vascara rất đẹp.', NOW()); 