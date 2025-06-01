package com.thoai.ecommerce_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thoai.ecommerce_service.entity.User;

// Annotation @Repository  đánh dấu là một bean chuyên xử lý truy xuất dữ liệu (Data Access Object - DAO)
// có thể không bắt buộc với interface extends JpaRepository, vì Spring Data JPA sẽ tự động tạo implementation và đăng
// ký bean
// Bắt buộc nếu cần xử lý thêm custom logic hoặc thêm annotation xử lý transaction sau này.

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Kiểm tra xem tên đăng nhập đã tồn tại hay chưa
    boolean existsByUsername(String username);
    // Kiểm tra xem email đã tồn tại hay chưa
    boolean existsByEmail(String email);

    // Tìm kiếm người dùng theo tên đăng nhập
    // Nếu tìm thấy User thì trả về User  nếu không có thì trả về rỗng
    Optional<User> findByUsername(String username);
    // Tìm kiếm người dùng theo email
    Optional<User> findByEmail(String email);
}
