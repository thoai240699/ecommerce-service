# Hướng dẫn Test API cho Hệ thống Ecommerce

## Tổng quan

Thư mục này chứa các file JSON test cho tất cả API endpoints trong hệ thống ecommerce. Mỗi file tương ứng với một controller và chứa các test case chi tiết.

## Danh sách File Test

### 1. Authentication API Tests

**File:** `authentication-api-tests.json`
**Mô tả:** Test cases cho xác thực người dùng
**Endpoints:**

- `POST /auth/token` - Đăng nhập
- `POST /auth/introspect` - Kiểm tra token
- `POST /auth/logout` - Đăng xuất

### 2. User API Tests

**File:** `user-api-tests.json`
**Mô tả:** Test cases cho quản lý người dùng
**Endpoints:**

- `POST /users` - Tạo người dùng
- `GET /users` - Lấy danh sách người dùng
- `GET /users/{userId}` - Lấy thông tin người dùng theo ID
- `GET /users/myInfo` - Lấy thông tin cá nhân
- `PUT /users/{userId}` - Cập nhật người dùng
- `DELETE /users/{userId}` - Xóa người dùng

### 3. Permission API Tests

**File:** `permission-api-tests.json`
**Mô tả:** Test cases cho quản lý quyền hạn
**Endpoints:**

- `POST /permissions` - Tạo quyền hạn
- `GET /permissions` - Lấy danh sách quyền hạn
- `DELETE /permissions/{permission}` - Xóa quyền hạn

### 4. Role API Tests

**File:** `role-api-tests.json`
**Mô tả:** Test cases cho quản lý vai trò
**Endpoints:**

- `POST /roles` - Tạo vai trò
- `GET /roles` - Lấy danh sách vai trò
- `PUT /roles/{roleName}` - Cập nhật vai trò
- `DELETE /roles/{role}` - Xóa vai trò

### 5. Address API Tests

**File:** `address-api-tests.json`
**Mô tả:** Test cases cho quản lý địa chỉ
**Endpoints:**

- `POST /addresses` - Tạo địa chỉ
- `GET /addresses` - Lấy tất cả địa chỉ
- `GET /addresses/{userId}` - Lấy địa chỉ theo userId
- `GET /addresses/myAddresses` - Lấy địa chỉ của chính mình
- `PUT /addresses/{addressId}` - Cập nhật địa chỉ
- `DELETE /addresses/{addressId}` - Xóa địa chỉ

### 6. Order API Tests

**File:** `order-api-tests.json`
**Mô tả:** Test cases cho quản lý đơn hàng
**Endpoints:**

- `POST /orders` - Tạo đơn hàng
- `GET /orders` - Lấy tất cả đơn hàng
- `GET /orders/{orderId}` - Lấy đơn hàng theo ID
- `GET /orders/myOrders` - Lấy đơn hàng của chính mình
- `PUT /orders/{orderId}` - Cập nhật đơn hàng
- `DELETE /orders/{orderId}` - Xóa đơn hàng
- `PUT /orders/{orderId}/cancel` - Hủy đơn hàng
- `PUT /orders/{orderId}/approve` - Duyệt đơn hàng

## Cách sử dụng

### 1. Chuẩn bị môi trường

```bash
# Khởi động ứng dụng
./mvnw spring-boot:run

# Hoặc
java -jar target/ecommerce-service-*.jar
```

### 2. Lấy JWT Token

Trước khi test các API cần xác thực, bạn cần lấy JWT token:

```bash
curl -X POST http://localhost:8080/auth/token \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin"
  }'
```

### 3. Sử dụng Token trong các request

Thay thế `JWT_TOKEN_HERE` trong các file test bằng token thực tế:

```bash
curl -X GET http://localhost:8080/users \
  -H "Authorization: Bearer YOUR_ACTUAL_JWT_TOKEN" \
  -H "Content-Type: application/json"
```

### 4. Test với Postman

1. Import các file JSON vào Postman
2. Tạo environment variables:
   - `base_url`: http://localhost:8080
   - `jwt_token`: JWT token từ bước đăng nhập
3. Chạy collection tests

### 5. Test với curl

Sử dụng các test case trong file JSON để tạo curl commands:

```bash
# Ví dụ: Tạo user mới
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "name": "Test User",
    "email": "test@example.com",
    "phone": "0912345678"
  }'
```

## Quyền hạn và Vai trò

### Vai trò mặc định:

- **ADMIN**: Toàn quyền quản lý hệ thống
- **SHOP**: Quản lý sản phẩm và đơn hàng
- **USER**: Mua hàng và quản lý thông tin cá nhân

### Quyền hạn chính:

- **USER\_\***: Quản lý người dùng
- **ROLE\_\***: Quản lý vai trò
- **PERMISSION\_\***: Quản lý quyền hạn
- **ADDRESS\_\***: Quản lý địa chỉ
- **ORDER\_\***: Quản lý đơn hàng
- **SYSTEM_ADMIN**: Quyền quản trị hệ thống

## Mã lỗi thường gặp

| Mã lỗi | Mô tả                          |
| ------ | ------------------------------ |
| 200    | Thành công                     |
| 1000   | Lỗi validation dữ liệu đầu vào |
| 2401   | Tên đăng nhập đã tồn tại       |
| 2402   | Không tìm thấy người dùng      |
| 2403   | Không tìm thấy username        |
| 2404   | Mật khẩu không đúng            |
| 2405   | Email đã được sử dụng          |
| 2406   | Không có quyền truy cập        |
| 2407   | Chưa đăng nhập                 |
| 2408   | Không tìm thấy địa chỉ         |
| 2409   | Không tìm thấy đơn hàng        |
| 2410   | Không tìm thấy vai trò ADMIN   |
| 2499   | Lỗi chưa xác định              |

## Luồng test đề xuất

### 1. Authentication Flow

1. Đăng nhập với admin/admin
2. Kiểm tra token validity
3. Test đăng xuất

### 2. User Management Flow

1. Tạo user mới
2. Lấy danh sách users (admin)
3. Cập nhật thông tin user
4. Xóa user (admin)

### 3. Permission & Role Flow

1. Tạo permissions mới
2. Tạo role với permissions
3. Cập nhật role
4. Xóa role và permissions

### 4. Address Flow

1. Tạo địa chỉ cho user
2. Lấy danh sách địa chỉ
3. Cập nhật địa chỉ
4. Xóa địa chỉ

### 5. Order Flow

1. Tạo đơn hàng mới
2. Duyệt đơn hàng (shop/admin)
3. Cập nhật trạng thái đơn hàng
4. Hủy đơn hàng
5. Xóa đơn hàng (admin)

## Ghi chú quan trọng

1. **JWT Token**: Token có thời hạn 4 giờ, cần refresh khi hết hạn
2. **Quyền hạn**: Mỗi endpoint có quyền hạn riêng, kiểm tra kỹ trước khi test
3. **Validation**: Tất cả input đều được validate, kiểm tra các trường hợp edge case
4. **Database**: Một số test case có thể thay đổi dữ liệu, cần backup trước khi test
5. **Environment**: Đảm bảo database đã được setup với dữ liệu mẫu

## Troubleshooting

### Lỗi 401 Unauthorized

- Kiểm tra JWT token có hợp lệ không
- Kiểm tra token có hết hạn không
- Đảm bảo header Authorization đúng format

### Lỗi 403 Forbidden

- Kiểm tra user có quyền truy cập endpoint không
- Kiểm tra role và permissions của user

### Lỗi 404 Not Found

- Kiểm tra URL endpoint có đúng không
- Kiểm tra ID trong path parameter có tồn tại không

### Lỗi 400 Bad Request

- Kiểm tra format JSON request body
- Kiểm tra validation rules cho các trường input
