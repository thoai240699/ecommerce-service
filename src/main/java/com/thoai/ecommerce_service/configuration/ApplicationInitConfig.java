package com.thoai.ecommerce_service.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.thoai.ecommerce_service.constant.PredefinedRole;
import com.thoai.ecommerce_service.entity.Role;
import com.thoai.ecommerce_service.entity.User;
import com.thoai.ecommerce_service.exception.AppException;
import com.thoai.ecommerce_service.exception.ErrorCode;
import com.thoai.ecommerce_service.repository.RoleRepository;
import com.thoai.ecommerce_service.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                // Tạo các role mặc định nếu chưa có. Do Database đã có các role này, nên không
                // cần tạo lại.
                // roleRepository.save(Role.builder()
                // .name(PredefinedRole.USER_ROLE)
                // .description("Người dùng - Có thể mua hàng, quản lý đơn hàng và thông tin cá
                // nhân")
                // .build());
                // roleRepository.save(Role.builder()
                // .name(PredefinedRole.SHOP_ROLE)
                // .description("Cửa hàng - Có thể quản lý sản phẩm, xử lý đơn hàng")
                // .build());
                //
                // Role adminRole = roleRepository.save(Role.builder()
                // .name(PredefinedRole.ADMIN_ROLE)
                // .description("Quản trị viên - Có toàn quyền quản lý hệ thống")
                // .build());
                Role adminRole = roleRepository
                        .findById(PredefinedRole.ADMIN_ROLE)
                        .orElseThrow(() -> new AppException(ErrorCode.ADMIN_NOT_FOUND));

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("admin user created with default password: admin");
            }
        };
    }
}
