# Ecommerce Service / Dịch Vụ Thương Mại Điện Tử

[English](#english) | [Tiếng Việt](#tiếng-việt)

<a name="english"></a>

## English

A robust e-commerce backend service built with Spring Boot, providing comprehensive functionality for user management, order processing, and role-based access control.

### Features

#### Authentication & Authorization

- JWT-based authentication
- Role-based access control (RBAC)
- Token introspection and validation
- Secure password encryption
- Session management with token invalidation

#### User Management

- User registration and profile management
- Role assignment and permission management
- Address management for users
- Secure password handling

#### Order Management

- Complete order lifecycle management
- Order status tracking (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- Order approval workflow
- Order cancellation with status validation

#### Role-Based Access Control

- Predefined roles: USER, SHOP, ADMIN
- Granular permission system
- Role and permission management APIs
- Secure endpoint access control

### API Endpoints

#### Authentication

- `POST /auth/token` - Login and get JWT token
- `POST /auth/introspect` - Validate token
- `POST /auth/logout` - Invalidate token

#### Users

- `POST /users` - Create new user
- `GET /users` - Get all users (ADMIN only)
- `GET /users/{userId}` - Get user by ID
- `GET /users/myInfo` - Get current user info
- `PUT /users/{userId}` - Update user
- `DELETE /users/{userId}` - Delete user

#### Orders

- `POST /orders` - Create new order
- `GET /orders` - Get all orders (ADMIN/SHOP)
- `GET /orders/{orderId}` - Get order by ID
- `GET /orders/myOrders` - Get user's orders
- `PUT /orders/{orderId}` - Update order
- `DELETE /orders/{orderId}` - Delete order
- `PUT /orders/{orderId}/cancel` - Cancel order
- `PUT /orders/{orderId}/approve` - Approve order

#### Addresses

- `POST /addresses` - Create new address
- `GET /addresses` - Get all addresses (ADMIN)
- `GET /addresses/{userId}` - Get addresses by user ID
- `GET /addresses/myAddresses` - Get user's addresses
- `PUT /addresses/{addressId}` - Update address
- `DELETE /addresses/{addressId}` - Delete address

#### Roles & Permissions

- `POST /roles` - Create new role
- `GET /roles` - Get all roles
- `PUT /roles/{roleName}` - Update role
- `DELETE /roles/{role}` - Delete role
- `POST /permissions` - Create new permission
- `GET /permissions` - Get all permissions
- `DELETE /permissions/{permission}` - Delete permission

### Security

The system implements several security measures:

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- Token introspection
- Secure session management
- Input validation
- Exception handling

### Error Handling

The system uses a standardized error response format:

```json
{
  "code": 2401,
  "message": "Error message",
  "result": null
}
```

Common error codes:

- 2401: User exists
- 2402: User not found
- 2403: Username not found
- 2404: Password mismatch
- 2405: Email exists
- 2406: Unauthorized access
- 2407: Unauthenticated
- 2408: Address not found
- 2409: Order not found
- 2410: Admin role not found

### Database Schema

The system uses the following main entities:

- Users (nguoi_dung)
- Roles (vai_tro)
- Permissions (quyen_han)
- Orders (don_hang)
- Addresses (dia_chi)
- Invalidated Tokens (token_da_huy)

### Getting Started

#### Prerequisites

- Java 17 or higher
- Maven
- MySQL database

#### Configuration

1. Configure database connection in `application.properties`
2. Set JWT signing key in `application.properties`
3. Configure other application properties as needed

#### Running the Application

```bash
mvn spring-boot:run
```

### Default Admin Account

- Username: admin
- Password: admin

**Note:** Change the default admin password after first login.

### License

This project is licensed under the MIT License.

---

<a name="tiếng-việt"></a>

## Tiếng Việt

Một dịch vụ backend thương mại điện tử mạnh mẽ được xây dựng bằng Spring Boot, cung cấp đầy đủ chức năng quản lý người dùng, xử lý đơn hàng và kiểm soát truy cập dựa trên vai trò.

### Tính Năng

#### Xác Thực & Phân Quyền

- Xác thực dựa trên JWT
- Kiểm soát truy cập dựa trên vai trò (RBAC)
- Kiểm tra và xác thực token
- Mã hóa mật khẩu an toàn
- Quản lý phiên với khả năng vô hiệu hóa token

#### Quản Lý Người Dùng

- Đăng ký và quản lý hồ sơ người dùng
- Phân công vai trò và quản lý quyền hạn
- Quản lý địa chỉ người dùng
- Xử lý mật khẩu an toàn

#### Quản Lý Đơn Hàng

- Quản lý toàn bộ vòng đời đơn hàng
- Theo dõi trạng thái đơn hàng (CHỜ XỬ LÝ, ĐANG XỬ LÝ, ĐANG GIAO, ĐÃ GIAO, ĐÃ HỦY)
- Quy trình duyệt đơn hàng
- Hủy đơn hàng với kiểm tra trạng thái

#### Kiểm Soát Truy Cập Dựa Trên Vai Trò

- Các vai trò mặc định: USER, SHOP, ADMIN
- Hệ thống phân quyền chi tiết
- API quản lý vai trò và quyền hạn
- Kiểm soát truy cập endpoint an toàn

### API Endpoints

#### Xác Thực

- `POST /auth/token` - Đăng nhập và lấy token JWT
- `POST /auth/introspect` - Xác thực token
- `POST /auth/logout` - Vô hiệu hóa token

#### Người Dùng

- `POST /users` - Tạo người dùng mới
- `GET /users` - Lấy danh sách người dùng (chỉ ADMIN)
- `GET /users/{userId}` - Lấy thông tin người dùng theo ID
- `GET /users/myInfo` - Lấy thông tin người dùng hiện tại
- `PUT /users/{userId}` - Cập nhật người dùng
- `DELETE /users/{userId}` - Xóa người dùng

#### Đơn Hàng

- `POST /orders` - Tạo đơn hàng mới
- `GET /orders` - Lấy tất cả đơn hàng (ADMIN/SHOP)
- `GET /orders/{orderId}` - Lấy đơn hàng theo ID
- `GET /orders/myOrders` - Lấy đơn hàng của người dùng
- `PUT /orders/{orderId}` - Cập nhật đơn hàng
- `DELETE /orders/{orderId}` - Xóa đơn hàng
- `PUT /orders/{orderId}/cancel` - Hủy đơn hàng
- `PUT /orders/{orderId}/approve` - Duyệt đơn hàng

#### Địa Chỉ

- `POST /addresses` - Tạo địa chỉ mới
- `GET /addresses` - Lấy tất cả địa chỉ (ADMIN)
- `GET /addresses/{userId}` - Lấy địa chỉ theo ID người dùng
- `GET /addresses/myAddresses` - Lấy địa chỉ của người dùng
- `PUT /addresses/{addressId}` - Cập nhật địa chỉ
- `DELETE /addresses/{addressId}` - Xóa địa chỉ

#### Vai Trò & Quyền Hạn

- `POST /roles` - Tạo vai trò mới
- `GET /roles` - Lấy danh sách vai trò
- `PUT /roles/{roleName}` - Cập nhật vai trò
- `DELETE /roles/{role}` - Xóa vai trò
- `POST /permissions` - Tạo quyền hạn mới
- `GET /permissions` - Lấy danh sách quyền hạn
- `DELETE /permissions/{permission}` - Xóa quyền hạn

### Bảo Mật

Hệ thống triển khai nhiều biện pháp bảo mật:

- Xác thực dựa trên JWT
- Mã hóa mật khẩu sử dụng BCrypt
- Kiểm soát truy cập dựa trên vai trò
- Kiểm tra token
- Quản lý phiên an toàn
- Xác thực đầu vào
- Xử lý ngoại lệ

### Xử Lý Lỗi

Hệ thống sử dụng định dạng phản hồi lỗi chuẩn:

```json
{
  "code": 2401,
  "message": "Thông báo lỗi",
  "result": null
}
```

Mã lỗi thường gặp:

- 2401: Người dùng đã tồn tại
- 2402: Không tìm thấy người dùng
- 2403: Không tìm thấy tên đăng nhập
- 2404: Mật khẩu không khớp
- 2405: Email đã tồn tại
- 2406: Truy cập không được phép
- 2407: Chưa xác thực
- 2408: Không tìm thấy địa chỉ
- 2409: Không tìm thấy đơn hàng
- 2410: Không tìm thấy vai trò ADMIN

### Cấu Trúc Cơ Sở Dữ Liệu

Hệ thống sử dụng các thực thể chính sau:

- Người dùng (nguoi_dung)
- Vai trò (vai_tro)
- Quyền hạn (quyen_han)
- Đơn hàng (don_hang)
- Địa chỉ (dia_chi)
- Token đã hủy (token_da_huy)

### Bắt Đầu

#### Yêu Cầu

- Java 17 trở lên
- Maven
- Cơ sở dữ liệu MySQL

#### Cấu Hình

1. Cấu hình kết nối cơ sở dữ liệu trong `application.properties`
2. Thiết lập khóa ký JWT trong `application.properties`
3. Cấu hình các thuộc tính ứng dụng khác nếu cần

#### Chạy Ứng Dụng

```bash
mvn spring-boot:run
```

### Tài Khoản Admin Mặc Định

- Tên đăng nhập: admin
- Mật khẩu: admin

**Lưu ý:** Thay đổi mật khẩu admin mặc định sau lần đăng nhập đầu tiên.

### Giấy Phép

Dự án này được cấp phép theo MIT License.
