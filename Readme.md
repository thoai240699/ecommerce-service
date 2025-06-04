ĐẠI HỌC QUỐC GIA TP. HỒ CHÍ MINH

TRƯỜNG ĐẠI HỌC CÔNG NGHỆ THÔNG TIN

ĐỀ TÀI: XÂY DỰNG WEB SERVICE BẰNG SPRING BOOT CHO ECOMMERCE WEBSITE

TP. HỒ CHÍ MINH – 04/2025

GIỚI THIỆU

Đề tài "Xây dựng web service bằng Spring Boot cho ecommerce website" nhằm phát triển một hệ thống backend API phục vụ cho website thương mại điện tử. Hệ thống cung cấp các chức năng cốt lõi bao gồm quản lý người dùng, xác thực phân quyền, quản lý đơn hàng và địa chỉ giao hàng.

**Công nghệ sử dụng:**

- Ngôn ngữ lập trình: Java 21
- Framework: Spring Boot 3.4.5
- Cơ sở dữ liệu: MySQL
- Bảo mật: Spring Security với JWT
- ORM: JPA/Hibernate
- Mapping: MapStruct
- Build tool: Maven

**Kết quả đạt được:**
Hệ thống đã triển khai thành công các chức năng quản lý người dùng, xác thực JWT, phân quyền RBAC, quản lý đơn hàng và địa chỉ với kiến trúc 3 lớp rõ ràng và bảo mật cao.

**Cam kết:**
Đề tài do sinh viên tự phân tích thiết kế và triển khai.

MÔ TẢ CƠ SỞ DỮ LIỆU

Cơ sở dữ liệu được thiết kế theo mô hình quan hệ (RDBMS) nhằm lưu trữ và quản lý dữ liệu hiệu quả cho hệ thống thương mại điện tử. Thiết kế tuân thủ nguyên tắc ACID và chuẩn hóa theo Third Normal Form (3NF) để đảm bảo tính toàn vẹn và giảm thiểu dự thừa dữ liệu.

Cơ sở dữ liệu hiện tại bao gồm 6 bảng chính được thiết kế theo mô hình RBAC (Role-Based Access Control) và các chức năng cơ bản của e-commerce:

**Thực thể người dùng (nguoi_dung)**

Chức năng: Lưu trữ thông tin tài khoản của tất cả người dùng hệ thống.

Các thuộc tính:

- ma_nguoi_dung (Khóa chính): Mã định danh duy nhất (UUID)
- ten_dang_nhap: Tên đăng nhập, duy nhất
- mat_khau: Mật khẩu đã mã hóa BCrypt
- ho_ten: Họ tên đầy đủ
- email: Địa chỉ email, duy nhất
- so_dien_thoai: Số điện thoại
- thoi_gian_tao: Timestamp tạo tài khoản

Mối quan hệ:

- 1-n với Address: Một người dùng có nhiều địa chỉ
- 1-n với Order: Một người dùng có nhiều đơn hàng
- n-n với Role: Một người dùng có nhiều vai trò

**Thực thể vai trò (vai_tro)**

Chức năng: Lưu trữ thông tin vai trò hệ thống (USER, SHOP, ADMIN).

Các thuộc tính:

- ten_vai_tro (Khóa chính): Tên vai trò duy nhất
- mo_ta: Mô tả vai trò

Mối quan hệ:

- n-n với User qua bảng nguoi_dung_vai_tro
- n-n với Permission qua bảng vai_tro_quyen_han

**Thực thể quyền hạn (quyen_han)**

Chức năng: Lưu trữ các quyền hạn cụ thể của hệ thống.

Các thuộc tính:

- ten_quyen_han (Khóa chính): Tên quyền hạn duy nhất
- mo_ta: Mô tả quyền hạn

**Thực thể địa chỉ (dia_chi)**

Chức năng: Lưu trữ địa chỉ giao hàng của người dùng.

Các thuộc tính:

- ma_dia_chi (Khóa chính): Mã định danh địa chỉ (UUID)
- ma_nguoi_dung (Khóa ngoại): Tham chiếu đến người dùng
- so_nha: Số nhà, tên đường
- phuong_xa: Phường/xã
- quan_huyen: Quận/huyện
- tinh_thanh: Tỉnh/thành phố

**Thực thể đơn hàng (don_hang)**

Chức năng: Lưu trữ thông tin đơn hàng.

Các thuộc tính:

- ma_don_hang (Khóa chính): Mã định danh đơn hàng (UUID)
- ma_nguoi_dung (Khóa ngoại): Người đặt hàng
- ma_dia_chi (Khóa ngoại): Địa chỉ giao hàng
- tong_tien: Tổng số tiền (BigDecimal)
- trang_thai: Trạng thái đơn hàng (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED)
- thoi_gian_tao: Thời điểm tạo đơn hàng

**Thực thể token đã hủy (token_da_huy)**

Chức năng: Lưu trữ các JWT token đã bị vô hiệu hóa khi logout.

Các thuộc tính:

- ma_token (Khóa chính): ID của token
- thoi_gian_het_han: Thời gian hết hạn token

THIẾT KẾ HỆ THỐNG

**Kiến trúc hệ thống**

Hệ thống được thiết kế theo kiến trúc 3 lớp (Three-layer Architecture):

**Controller Layer (Presentation):**

- UserController: Quản lý tài khoản người dùng
- AuthenticationController: Xác thực và JWT token
- RoleController: Quản lý vai trò
- PermissionController: Quản lý quyền hạn
- AddressController: Quản lý địa chỉ
- OrderController: Quản lý đơn hàng

**Service Layer (Business Logic):**

- UserService: Logic nghiệp vụ người dùng
- AuthenticationService: Logic xác thực JWT
- RoleService: Logic quản lý vai trò
- PermissionService: Logic quản lý quyền hạn
- AddressService: Logic quản lý địa chỉ
- OrderService: Logic quản lý đơn hàng

**Repository Layer (Data Access):**

- UserRepository: Truy xuất dữ liệu User
- RoleRepository: Truy xuất dữ liệu Role
- PermissionRepository: Truy xuất dữ liệu Permission
- AddressRepository: Truy xuất dữ liệu Address
- OrderRepository: Truy xuất dữ liệu Order
- InvalidatedTokenRepository: Quản lý token blacklist

**Các thành phần hỗ trợ:**

- Mapper (MapStruct): Chuyển đổi Entity ↔ DTO
- SecurityConfig: Cấu hình Spring Security
- GlobalExceptionHandler: Xử lý lỗi toàn cục
- CustomJwtDecoder: JWT decoder tùy chỉnh

**ỨNG DỤNG MÔ HÌNH MVC**

Hệ thống áp dụng mô hình MVC (Model-View-Controller) với kiến trúc RESTful API:

**Controller Layer:**

- Tiếp nhận HTTP request từ client
- Trả về response theo định dạng chuẩn: {code, message, result}
- Thực hiện validation input và định tuyến request đến Service

**Service Layer (Business Logic):**

- Xử lý nghiệp vụ và logic ứng dụng
- Validation dữ liệu business
- Mã hóa mật khẩu với BCrypt
- Sinh và quản lý JWT token
- Xử lý phân quyền và authorization

**Repository Layer (Model/Data Access):**

- Truy xuất dữ liệu với JPA/Hibernate
- Thực hiện các operations CRUD
- Quản lý database transactions

**Exception Handling:**
Khi có lỗi xảy ra, hệ thống sẽ trả về response có ý nghĩa với mã lỗi và message tương ứng thông qua GlobalExceptionHandler.

**Sử dụng Lombok và MapStruct:**

**Lombok:** Tự động generate getter, setter, constructor khi compile, giúp giảm boilerplate code và dễ bảo trì. Khi dự án phức tạp với nhiều thuộc tính, Lombok giúp code trở nên sạch sẽ và dễ đọc hơn. Khi chạy sẽ tạo ra các method getter/setter trong thư mục target.

**MapStruct:** Tự động ánh xạ giữa Entity và DTO có cùng tên thuộc tính, giảm thiểu code mapping thủ công và tăng hiệu suất runtime.

**XÂY DỰNG API XÁC THỰC TÀI KHOẢN**

**Mã hóa mật khẩu:**
Hệ thống sử dụng thuật toán hash BCrypt để mã hóa mật khẩu trong bảng nguoi_dung. Sử dụng interface PasswordEncoder được cung cấp thông qua dependency spring-security-crypto với strength = 8.

Ví dụ: mật khẩu "thoai123xyz" sẽ được mã hóa thành:
"$2a$08$oLtkcfuozCndWoHbwzmW6OG8TnKP2.h/pdMKCiOLOyqcnwB0fUTtK"

**Triển khai JWT Authentication:**

Trong triển khai thực tế, không thể bắt người dùng đăng nhập lại mỗi khi chuyển trang, vì vậy JWT (JSON Web Token) được sử dụng để duy trì phiên đăng nhập.

Dự án tiến hành triển khai JWT bằng thư viện nimbus-jose-jwt (được tích hợp trong spring-boot-starter-oauth2-resource-server).

**JWT Implementation Details:**

- **Thư viện:** nimbus-jose-jwt (tích hợp sẵn trong OAuth2 Resource Server)
- **Thuật toán:** HS512 (HMAC with SHA-512)
- **Thời hạn token:** 4 giờ
- **Cấu trúc token:** Header + Payload + Signature

**Thiết kế JWT Token:**

Khi đăng nhập thành công, hệ thống trả về response chứa JWT token:

```json
{
  "code": 200,
  "message": null,
  "result": {
    "token": "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6InRob2FpLmNvbSIsImlhdCI6MTczNzM1NTIwMCwiZXhwIjoxNzM3MzY5NjAwLCJqdGkiOiI1YjMxNGUzNi0yZjQyLTQ3YWUtOGVkYy1mYTdkMjU4NjQxNzEiLCJzY29wZSI6IlJPTEVfQURNSU4gU1lTVEVNX0FETUlOIFVTRVJfQ1JFQVRFIFVTRVJfUkVBRCJ9.signature_here"
  }
}
```

**JWT Token Payload chứa:**

- **sub (subject):** Username của người dùng
- **iss (issuer):** "thoai.com" - domain phát hành token
- **iat (issued at):** Thời gian phát hành token
- **exp (expiration):** Thời gian hết hạn token (4 giờ từ lúc phát hành)
- **jti (JWT ID):** ID duy nhất của token (UUID)
- **scope:** Chuỗi chứa vai trò và quyền hạn (VD: "ROLE_ADMIN SYSTEM_ADMIN USER_CREATE USER_READ")

Token có thể được decode tại https://jwt.io/ để xem thông tin chi tiết, hiển thị đầy đủ thuật toán, issuer, username, thời gian hết hạn, và các quyền hạn được thiết kế trong source code.

**API ENDPOINTS**

Base URL: `http://localhost:8080/ecommerce`

**Authentication API (/auth):**

- POST /auth/token: Đăng nhập và nhận JWT token
- POST /auth/introspect: Kiểm tra tính hợp lệ của token
- POST /auth/logout: Đăng xuất và vô hiệu hóa token

**User Management API (/users):**

- POST /users: Tạo tài khoản mới (public)
- GET /users: Lấy danh sách người dùng (admin only)
- GET /users/{userId}: Lấy thông tin người dùng theo ID
- GET /users/myInfo: Lấy thông tin cá nhân
- PUT /users/{userId}: Cập nhật thông tin người dùng
- DELETE /users/{userId}: Xóa người dùng (admin only)

**Role Management API (/roles):**

- POST /roles: Tạo vai trò mới (admin only)
- GET /roles: Lấy danh sách vai trò
- PUT /roles/{roleName}: Cập nhật vai trò
- DELETE /roles/{role}: Xóa vai trò (admin only)

**Permission Management API (/permissions):**

- POST /permissions: Tạo quyền hạn mới (admin only)
- GET /permissions: Lấy danh sách quyền hạn
- DELETE /permissions/{permission}: Xóa quyền hạn (admin only)

**Address Management API (/addresses):**

- POST /addresses: Tạo địa chỉ mới
- GET /addresses: Lấy tất cả địa chỉ (admin only)
- GET /addresses/{userId}: Lấy địa chỉ theo user ID (admin only)
- GET /addresses/myAddresses: Lấy địa chỉ cá nhân
- PUT /addresses/{addressId}: Cập nhật địa chỉ
- DELETE /addresses/{addressId}: Xóa địa chỉ

**Order Management API (/orders):**

- POST /orders: Tạo đơn hàng mới
- GET /orders: Lấy tất cả đơn hàng (admin/shop only)
- GET /orders/{orderId}: Lấy đơn hàng theo ID
- GET /orders/myOrders: Lấy đơn hàng cá nhân
- PUT /orders/{orderId}: Cập nhật đơn hàng (admin/shop only)
- DELETE /orders/{orderId}: Xóa đơn hàng (admin only)
- PUT /orders/{orderId}/cancel: Hủy đơn hàng
- PUT /orders/{orderId}/approve: Duyệt đơn hàng (admin/shop only)

**BẢO MẬT VÀ PHÂN QUYỀN**

**Mô hình RBAC:**

- **ADMIN**: Toàn quyền hệ thống
- **SHOP**: Quản lý đơn hàng và duyệt đơn
- **USER**: Khách hàng thông thường

**Các quyền hạn chính:**

- USER_CREATE, USER_READ, USER_READ_ALL, USER_UPDATE, USER_DELETE
- ROLE_CREATE, ROLE_READ, ROLE_UPDATE, ROLE_DELETE
- PERMISSION_CREATE, PERMISSION_READ, PERMISSION_DELETE
- ADDRESS_CREATE, ADDRESS_READ, ADDRESS_READ_ALL, ADDRESS_UPDATE, ADDRESS_DELETE
- ORDER_CREATE, ORDER_READ, ORDER_READ_ALL, ORDER_UPDATE, ORDER_DELETE, ORDER_CANCEL, ORDER_APPROVE
- SYSTEM_ADMIN (quyền đặc biệt cho admin)

**JWT Security:**

- Token thời hạn 4 giờ
- Thuật toán HS512 với nimbus-jose-jwt
- Chứa username, roles, permissions trong scope
- Hỗ trợ introspection và blacklist khi logout

**Mã hóa mật khẩu:**
Sử dụng BCrypt với strength = 8 thông qua Spring Security Crypto.

Ví dụ: mật khẩu "thoai123xyz" được mã hóa thành:
"$2a$08$oLtkcfuozCndWoHbwzmW6OG8TnKP2.h/pdMKCiOLOyqcnwB0fUTtK"

KẾT LUẬN

**Kết quả đạt được:**

Dự án đã xây dựng thành công một web service API hoàn chỉnh cho hệ thống thương mại điện tử với các tính năng:

**Quản lý người dùng và xác thực:**

- Đăng ký/đăng nhập với validation đầy đủ
- Mã hóa mật khẩu BCrypt strength 8
- JWT authentication với thời hạn 4 giờ
- Phân quyền RBAC với 3 vai trò chính

**Quản lý đơn hàng và địa chỉ:**

- CRUD operations hoàn chỉnh cho đơn hàng và địa chỉ
- Workflow đơn hàng: PENDING → PROCESSING → SHIPPED → DELIVERED
- Phân quyền chi tiết cho từng thao tác

**Kiến trúc và kỹ thuật:**

- Three-layer Architecture rõ ràng
- RESTful API với response format chuẩn
- Global Exception Handling
- MapStruct auto-mapping
- Bean Validation với custom message tiếng Việt

**Cơ sở dữ liệu:**

- Thiết kế chuẩn hóa 3NF
- JPA/Hibernate ORM
- Auto DDL update
- UUID primary keys

**Bảo mật:**

- Spring Security integration
- JWT với custom decoder
- Token introspection và blacklist
- Method-level security với @PreAuthorize

**Chức năng hài lòng nhất:**

Hệ thống xác thực JWT kết hợp phân quyền RBAC là điểm mạnh của dự án. CustomJwtDecoder với introspection endpoint, token blacklist và phân quyền method-level tạo ra giải pháp bảo mật mạnh mẽ và linh hoạt.

**Hướng phát triển:**

Sẽ tiếp tục triển khai các module sản phẩm, danh mục, giỏ hàng, thanh toán và tích hợp các dịch vụ bên ngoài.

TÀI LIỆU THAM KHẢO

1. Spring Boot Documentation, Spring Team, 2024. https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/

2. Spring Security Reference, Spring Security Team, 2024. https://docs.spring.io/spring-security/reference/

3. JWT.io - JSON Web Tokens Introduction, Auth0, 2024. https://jwt.io/introduction/

4. MySQL 8.0 Reference Manual, Oracle Corporation, 2024. https://dev.mysql.com/doc/refman/8.0/en/

5. MapStruct Reference Guide, MapStruct Team, 2024. https://mapstruct.org/documentation/stable/reference/html/

6. Silberschatz, Abraham, et al., Database System Concepts, 7th Edition, McGraw-Hill Education, 2019.

7. Craig Walls, Spring Boot in Action, 2nd Edition, Manning Publications, 2018.

PHỤ LỤC PHÂN CÔNG NHIỆM VỤ

**Sinh viên thực hiện:** [Tên sinh viên]

**Nhiệm vụ đã thực hiện:**

1. **Thiết kế cơ sở dữ liệu:**

   - Phân tích nghiệp vụ và thiết kế ER diagram
   - Chuẩn hóa database theo 3NF
   - Thiết kế RBAC model

2. **Backend Development:**

   - Setup Spring Boot 3.4.5 project với Maven
   - Implement JPA Entities với validation
   - Xây dựng Repository layer với Spring Data JPA
   - Phát triển Service layer với business logic
   - Tạo REST Controllers với proper HTTP methods

3. **Security Implementation:**

   - JWT authentication với HS512
   - RBAC authorization với method-level security
   - BCrypt password encoding
   - Custom JWT decoder và token introspection

4. **Testing và Documentation:**
   - API testing với Postman
   - Exception handling và validation
   - Code documentation và deployment guide

**Sản phẩm:**

- Báo cáo đồ án (Word/PDF)
- Source code hoàn chỉnh
- Database scripts
- API documentation
- Demo presentation

PHỤ LỤC DEMO

**Yêu cầu hệ thống:**

- JDK 21+
- MySQL Server 8.0+
- Maven 3.8+
- IDE: IntelliJ IDEA hoặc VS Code

**Bước 1: Chuẩn bị database**

```sql
CREATE DATABASE ecommerce_service;
CREATE USER 'your_username'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON ecommerce_service.* TO 'your_username'@'localhost';
```

**Bước 2: Cấu hình application.yaml**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3308/ecommerce_service
    username: your_username
    password: your_password
```

**Bước 3: Build và chạy**

```bash
git clone [repository-url]
cd ecommerce-service
mvn clean install
mvn spring-boot:run
```

**Bước 4: Test API**
Base URL: `http://localhost:8080/ecommerce`

1. **Đăng nhập admin:**

   ```
   POST /ecommerce/auth/token
   Body: {"username": "admin", "password": "admin"}
   ```

2. **Tạo user mới:**

   ```
   POST /ecommerce/users
   Body: {
     "username": "user1",
     "password": "password123",
     "email": "user1@test.com",
     "name": "Test User",
     "phone": "0901234567"
   }
   ```

3. **Lấy thông tin cá nhân:**

   ```
   GET /ecommerce/users/myInfo
   Authorization: Bearer {token}
   ```

4. **Tạo địa chỉ:**

   ```
   POST /ecommerce/addresses
   Authorization: Bearer {token}
   Body: {
     "userId": "{user-id}",
     "streetAddress": "123 Nguyen Van Linh",
     "ward": "Tan Hung",
     "district": "Quan 7",
     "city": "Ho Chi Minh"
   }
   ```

5. **Tạo đơn hàng:**
   ```
   POST /ecommerce/orders
   Authorization: Bearer {token}
   Body: {
     "userId": "{user-id}",
     "addressId": "{address-id}",
     "totalAmount": 500000
   }
   ```

PHỤ LỤC HÌNH

**Hình 1: Sơ đồ lớp hệ thống (Class Diagram)**

Sơ đồ UML class diagram thể hiện kiến trúc 3 lớp của hệ thống:

- **Entity Layer:** User, Role, Permission, Address, Order, InvalidatedToken, OrderStatus
- **Controller Layer:** UserController, AuthenticationController, RoleController, PermissionController, AddressController, OrderController
- **Service Layer:** UserService, AuthenticationService, RoleService, PermissionService, AddressService, OrderService
- **Repository Layer:** UserRepository, RoleRepository, PermissionRepository, AddressRepository, OrderRepository, InvalidatedTokenRepository
- **Configuration Layer:** SecurityConfig, CustomJwtDecoder, ApplicationInitConfig
- **Exception Layer:** GlobalExceptionHandler, AppException

Sơ đồ hiển thị mối quan hệ dependency injection giữa các lớp và data flow từ Controller → Service → Repository → Entity.

**Hình 2: Cây sơ đồ thư mục (Directory Structure)**

Cấu trúc thư mục dự án Spring Boot Maven:

```
ecommerce-service/
├── Doccument/
│   └── Readme.md
├── src/
│   ├── main/
│   │   ├── java/com/thoai/ecommerce_service/
│   │   │   ├── configuration/
│   │   │   │   ├── ApplicationInitConfig.java
│   │   │   │   ├── CustomJwtDecoder.java
│   │   │   │   ├── JwtAuthenticationEntryPoint.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── constant/
│   │   │   │   └── PredefinedRole.java
│   │   │   ├── controller/
│   │   │   │   ├── AddressController.java
│   │   │   │   ├── AuthenticationController.java
│   │   │   │   ├── OrderController.java
│   │   │   │   ├── PermissionController.java
│   │   │   │   ├── RoleController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   ├── request/
│   │   │   │   │   ├── AddressCreateRequest.java
│   │   │   │   │   ├── AddressUpdateRequest.java
│   │   │   │   │   ├── AuthenticationRequest.java
│   │   │   │   │   ├── IntrospectRequest.java
│   │   │   │   │   ├── LogoutRequest.java
│   │   │   │   │   ├── OrderCreationRequest.java
│   │   │   │   │   ├── OrderUpdateRequest.java
│   │   │   │   │   ├── PermissionRequest.java
│   │   │   │   │   ├── RoleRequest.java
│   │   │   │   │   ├── UserCreationRequest.java
│   │   │   │   │   └── UserUpdateRequest.java
│   │   │   │   └── response/
│   │   │   │       ├── AddressReponse.java
│   │   │   │       ├── ApiResponse.java
│   │   │   │       ├── AuthenticationResponse.java
│   │   │   │       ├── IntrospectResponse.java
│   │   │   │       ├── OrderResponse.java
│   │   │   │       ├── PermissionResponse.java
│   │   │   │       ├── RoleResponse.java
│   │   │   │       └── UserResponse.java
│   │   │   ├── entity/
│   │   │   │   ├── Address.java
│   │   │   │   ├── InvalidatedToken.java
│   │   │   │   ├── Order.java
│   │   │   │   ├── Permission.java
│   │   │   │   ├── Role.java
│   │   │   │   └── User.java
│   │   │   ├── enums/
│   │   │   │   └── OrderStatus.java
│   │   │   ├── exception/
│   │   │   │   ├── AppException.java
│   │   │   │   ├── ErrorCode.java
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── mapper/
│   │   │   │   ├── AddressMapper.java
│   │   │   │   ├── OrderMapper.java
│   │   │   │   ├── PermissionMapper.java
│   │   │   │   ├── RoleMapper.java
│   │   │   │   └── UserMapper.java
│   │   │   ├── repository/
│   │   │   │   ├── AddressRepository.java
│   │   │   │   ├── InvalidatedTokenRepository.java
│   │   │   │   ├── OrderRepository.java
│   │   │   │   ├── PermissionRepository.java
│   │   │   │   ├── RoleRepository.java
│   │   │   │   └── UserRepository.java
│   │   │   ├── service/
│   │   │   │   ├── AddressService.java
│   │   │   │   ├── AuthenticationService.java
│   │   │   │   ├── OrderService.java
│   │   │   │   ├── PermissionService.java
│   │   │   │   ├── RoleService.java
│   │   │   │   └── UserService.java
│   │   │   └── EcommerceServiceApplication.java
│   │   └── resources/
│   │       └── application.yaml
│   └── test/
├── target/
├── mvnw
├── mvnw.cmd
└── pom.xml
```

**Hình 3: Site-map API Endpoints**

Bản đồ tổng quan tất cả API endpoints của hệ thống:

**Base URL:** `http://localhost:8080/ecommerce`

**Authentication APIs (/auth):**

- POST /auth/token - Đăng nhập và lấy JWT token
- POST /auth/introspect - Kiểm tra tính hợp lệ token
- POST /auth/logout - Đăng xuất và vô hiệu hóa token

**User Management APIs (/users):**

- POST /users - Tạo tài khoản (public)
- GET /users - Lấy danh sách user (admin)
- GET /users/{userId} - Lấy user theo ID
- GET /users/myInfo - Lấy thông tin cá nhân
- PUT /users/{userId} - Cập nhật user
- DELETE /users/{userId} - Xóa user (admin)

**Role Management APIs (/roles):**

- POST /roles - Tạo role (admin)
- GET /roles - Lấy danh sách role
- PUT /roles/{roleName} - Cập nhật role (admin)
- DELETE /roles/{role} - Xóa role (admin)

**Permission Management APIs (/permissions):**

- POST /permissions - Tạo permission (admin)
- GET /permissions - Lấy danh sách permission
- DELETE /permissions/{permission} - Xóa permission (admin)

**Address Management APIs (/addresses):**

- POST /addresses - Tạo địa chỉ
- GET /addresses - Lấy tất cả địa chỉ (admin)
- GET /addresses/{userId} - Lấy địa chỉ theo user (admin)
- GET /addresses/myAddresses - Lấy địa chỉ cá nhân
- PUT /addresses/{addressId} - Cập nhật địa chỉ
- DELETE /addresses/{addressId} - Xóa địa chỉ

**Order Management APIs (/orders):**

- POST /orders - Tạo đơn hàng
- GET /orders - Lấy tất cả đơn hàng (admin/shop)
- GET /orders/{orderId} - Lấy đơn hàng theo ID
- GET /orders/myOrders - Lấy đơn hàng cá nhân
- PUT /orders/{orderId} - Cập nhật đơn hàng (admin/shop)
- DELETE /orders/{orderId} - Xóa đơn hàng (admin)
- PUT /orders/{orderId}/cancel - Hủy đơn hàng
- PUT /orders/{orderId}/approve - Duyệt đơn hàng (admin/shop)

**Security Levels:**

- 🌐 Public: Không cần xác thực
- 🔒 Authenticated: Cần JWT token hợp lệ
- 🔑 Owner Access: User sở hữu resource
- 👑 Admin Only: Chỉ ADMIN role
- 🏪 Shop Access: SHOP hoặc ADMIN role

**Hình 4: API Response Format**

```json
{
  "code": 200,
  "message": "Success",
  "result": {
    // Dữ liệu response
  }
}
```

**Hình 5: JWT Token Structure**

```
Header: {"alg": "HS512", "typ": "JWT"}
Payload: {
  "sub": "username",
  "iss": "thoai.com",
  "iat": 1737355200,
  "exp": 1737369600,
  "jti": "5b314e36-2f42-47ae-8edc-fa7d25864171",
  "scope": "ROLE_ADMIN SYSTEM_ADMIN USER_CREATE USER_READ"
}
Signature: HMACSHA512(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
```

**Hình 6: RBAC Authorization Flow**

```
HTTP Request → JWT Filter → Extract Claims → Check Permissions → @PreAuthorize → Method Execution
     ↓              ↓               ↓                ↓                  ↓              ↓
  Bearer Token → Parse JWT → Get User Roles → Validate Authority → Allow/Deny → Business Logic
```

**Hình 7: Database Entity Relationships**

```
User (1) ←→ (n) Address
User (1) ←→ (n) Order
Order (n) ←→ (1) Address
User (n) ←→ (n) Role ←→ (n) Permission
Order ←→ OrderStatus (enum)
InvalidatedToken (standalone)
```
