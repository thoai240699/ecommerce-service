-- Thiết lập bộ ký tự và collation cho tiếng Việt
SET NAMES 'utf8mb4';
SET CHARACTER SET utf8mb4;
SET character_set_connection = utf8mb4;

-- Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
START TRANSACTION;

-- Tạm thời tắt kiểm tra khóa ngoại để có thể xóa dữ liệu
SET FOREIGN_KEY_CHECKS = 0;

-- Xóa dữ liệu cũ từ các bảng theo thứ tự ngược với quan hệ phụ thuộc
TRUNCATE TABLE danh_gia;
TRUNCATE TABLE thanh_toan;
TRUNCATE TABLE chi_tiet_don_hang;
TRUNCATE TABLE don_hang;
TRUNCATE TABLE hinh_anh_san_pham;
TRUNCATE TABLE san_pham;
TRUNCATE TABLE dia_chi;
TRUNCATE TABLE nguoi_dung;
TRUNCATE TABLE loai_hang;

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS = 1;

-- Thêm dữ liệu mẫu cho bảng loại hàng (bảng không phụ thuộc vào bảng nào)
INSERT INTO loai_hang (ma_loai_hang, ten_loai_hang) VALUES
('LH001', 'Điện thoại di động'),
('LH002', 'Laptop'),
('LH003', 'Máy tính bảng'),
('LH004', 'Phụ kiện điện thoại'),
('LH005', 'Tai nghe'),
('LH006', 'Loa'),
('LH007', 'Đồng hồ thông minh'),
('LH008', 'Máy ảnh'),
('LH009', 'Máy in'),
('LH010', 'Màn hình máy tính');

-- Thêm dữ liệu mẫu cho bảng người dùng (bảng không phụ thuộc vào bảng nào)
INSERT INTO nguoi_dung (ma_nguoi_dung, ten_dang_nhap, mat_khau, email, ho_ten, so_dien_thoai, vai_tro) VALUES
('ND001', 'admin', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'admin@example.com', 'Nguyễn Văn An', '0912345678', 'Quản trị viên'),
('ND002', 'nguyenvana', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'nguyenvana@gmail.com', 'Nguyễn Văn A', '0987654321', 'Người dùng'),
('ND003', 'tranthib', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'tranthib@gmail.com', 'Trần Thị B', '0376543210', 'Người dùng'),
('ND004', 'levanc', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'levanc@gmail.com', 'Lê Văn C', '0587654321', 'Người dùng'),
('ND005', 'phamthid', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'phamthid@gmail.com', 'Phạm Thị D', '0798765432', 'Người dùng'),
('ND006', 'hoangvane', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'hoangvane@gmail.com', 'Hoàng Văn E', '0898765432', 'Người dùng'),
('ND007', 'staff1', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'staff1@example.com', 'Trần Văn Staff', '0912345679', 'Nhân viên'),
('ND008', 'staff2', '$2a$10$rDkPvvAFV6GgJjXpzXqKXeXzXzXzXzXzXzXzXzXzXzXzXzXzXzXz', 'staff2@example.com', 'Lê Thị Staff', '0987654320', 'Nhân viên');

-- Thêm dữ liệu mẫu cho bảng địa chỉ (phụ thuộc vào bảng người dùng)
INSERT INTO dia_chi (ma_dia_chi, ma_nguoi_dung, so_nha, phuong_xa, quan_huyen, tinh_thanh) VALUES
('DC001', 'ND001', '123', 'Phường Bến Nghé', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC002', 'ND002', '456', 'Phường Đa Kao', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC003', 'ND003', '789', 'Phường Bến Thành', 'Quận 1', 'TP. Hồ Chí Minh'),
('DC004', 'ND004', '321', 'Phường 2', 'Quận 3', 'TP. Hồ Chí Minh'),
('DC005', 'ND005', '654', 'Phường 4', 'Quận 5', 'TP. Hồ Chí Minh'),
('DC006', 'ND006', '987', 'Phường 7', 'Quận 8', 'TP. Hồ Chí Minh'),
('DC007', 'ND007', '147', 'Phường 12', 'Quận Gò Vấp', 'TP. Hồ Chí Minh'),
('DC008', 'ND008', '258', 'Phường 9', 'Quận Phú Nhuận', 'TP. Hồ Chí Minh');

-- Thêm dữ liệu mẫu cho bảng sản phẩm (phụ thuộc vào bảng loại hàng)
INSERT INTO san_pham (ma_san_pham, ten_san_pham, mo_ta_san_pham, gia_san_pham, so_luong_ton, ma_loai_hang, thoi_gian_tao) VALUES
('SP001', 'iPhone 14 Pro Max', 'Điện thoại iPhone 14 Pro Max 256GB', 29990000, 50, 'LH001', NOW()),
('SP002', 'Samsung Galaxy S23 Ultra', 'Điện thoại Samsung Galaxy S23 Ultra 256GB', 26990000, 45, 'LH001', NOW()),
('SP003', 'MacBook Pro M2', 'Laptop MacBook Pro M2 13 inch', 32990000, 30, 'LH002', NOW()),
('SP004', 'iPad Pro 12.9', 'Máy tính bảng iPad Pro 12.9 inch 2023', 25990000, 25, 'LH003', NOW()),
('SP005', 'AirPods Pro 2', 'Tai nghe không dây AirPods Pro 2', 5990000, 100, 'LH005', NOW()),
('SP006', 'Samsung Galaxy Watch 6', 'Đồng hồ thông minh Samsung Galaxy Watch 6', 7990000, 40, 'LH007', NOW()),
('SP007', 'Sony WH-1000XM5', 'Tai nghe không dây Sony WH-1000XM5', 8990000, 35, 'LH005', NOW()),
('SP008', 'Dell XPS 13', 'Laptop Dell XPS 13 2023', 28990000, 20, 'LH002', NOW()),
('SP009', 'Canon EOS R6', 'Máy ảnh Canon EOS R6 Mark II', 45990000, 15, 'LH008', NOW()),
('SP010', 'HP LaserJet Pro', 'Máy in HP LaserJet Pro M404dn', 5990000, 30, 'LH009', NOW());

-- Thêm dữ liệu mẫu cho bảng hình ảnh sản phẩm (phụ thuộc vào bảng sản phẩm)
INSERT INTO hinh_anh_san_pham (ma_hinh_anh, ma_san_pham) VALUES
('HA001', 'SP001'),
('HA002', 'SP001'),
('HA003', 'SP002'),
('HA004', 'SP002'),
('HA005', 'SP003'),
('HA006', 'SP003'),
('HA007', 'SP004'),
('HA008', 'SP004'),
('HA009', 'SP005'),
('HA010', 'SP005');

-- Thêm dữ liệu mẫu cho bảng đơn hàng (phụ thuộc vào bảng người dùng và địa chỉ)
INSERT INTO don_hang (ma_don_hang, ma_nguoi_dung, ma_dia_chi, tong_tien, trang_thai, thoi_gian_tao) VALUES
('DH001', 'ND002', 'DC002', 35989000, 'Đã giao', NOW()),
('DH002', 'ND003', 'DC003', 32990000, 'Đang xử lý', NOW()),
('DH003', 'ND002', 'DC002', 5990000, 'Chờ xử lý', NOW()),
('DH004', 'ND004', 'DC004', 7990000, 'Đã giao', NOW()),
('DH005', 'ND005', 'DC005', 8990000, 'Đang xử lý', NOW());

-- Thêm dữ liệu mẫu cho bảng chi tiết đơn hàng (phụ thuộc vào bảng đơn hàng và sản phẩm)
INSERT INTO chi_tiet_don_hang (ma_ct_don_hang, ma_don_hang, ma_san_pham, so_luong, gia) VALUES
('CT001', 'DH001', 'SP001', 1, 29990000),
('CT002', 'DH001', 'SP005', 1, 5990000),
('CT003', 'DH002', 'SP003', 1, 32990000),
('CT004', 'DH003', 'SP005', 1, 5990000),
('CT005', 'DH004', 'SP006', 1, 7990000),
('CT006', 'DH005', 'SP007', 1, 8990000);

-- Thêm dữ liệu mẫu cho bảng thanh toán (phụ thuộc vào bảng đơn hàng)
INSERT INTO thanh_toan (ma_thanh_toan, ma_don_hang, phuong_thuc, trang_thai, ngay_thanh_toan) VALUES
('TT001', 'DH001', 'Chuyển khoản ngân hàng', 'Hoàn tất', NOW()),
('TT002', 'DH002', 'Thẻ tín dụng', 'Chờ xử lý', NULL),
('TT003', 'DH003', 'Ví điện tử', 'Chờ xử lý', NULL),
('TT004', 'DH004', 'Chuyển khoản ngân hàng', 'Hoàn tất', NOW()),
('TT005', 'DH005', 'Thẻ tín dụng', 'Chờ xử lý', NULL);

-- Thêm dữ liệu mẫu cho bảng đánh giá (phụ thuộc vào bảng người dùng và sản phẩm)
INSERT INTO danh_gia (ma_danh_gia, ma_nguoi_dung, ma_san_pham, diem_danh_gia, binh_luan, thoi_gian_tao) VALUES
('DG001', 'ND002', 'SP001', 5, 'Sản phẩm rất tốt, đóng gói cẩn thận', NOW()),
('DG002', 'ND002', 'SP005', 4, 'Âm thanh tốt, pin trâu', NOW()),
('DG003', 'ND003', 'SP003', 5, 'Máy mạnh, màn hình đẹp', NOW()),
('DG004', 'ND004', 'SP006', 4, 'Đồng hồ đẹp, pin trâu', NOW()),
('DG005', 'ND005', 'SP007', 5, 'Âm thanh tuyệt vời, chống ồn tốt', NOW());

-- Lưu tất cả các thay đổi vào database
COMMIT; 